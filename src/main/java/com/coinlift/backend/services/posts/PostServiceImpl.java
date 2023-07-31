package com.coinlift.backend.services.posts;

import com.coinlift.backend.config.s3.S3Buckets;
import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostShortResponseDto;
import com.coinlift.backend.entities.MyUserDetails;
import com.coinlift.backend.entities.Post;
import com.coinlift.backend.exceptions.DeniedAccessException;
import com.coinlift.backend.exceptions.ResourceNotFoundException;
import com.coinlift.backend.mappers.PostMapper;
import com.coinlift.backend.repositories.PostRepository;
import com.coinlift.backend.repositories.UserRepository;
import com.coinlift.backend.services.s3.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private final S3Service s3Service;

    private final S3Buckets s3Buckets;

    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper, S3Service s3Service, S3Buckets s3Buckets, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.s3Service = s3Service;
        this.s3Buckets = s3Buckets;
        this.userRepository = userRepository;
    }

    private Post getPost(UUID postId) {
        return postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post with id [%s] not found".formatted(postId)));
    }

    /**
     * Retrieves the latest posts.
     *
     * @return A list of `PostShortResponseDto` representing the latest posts.
     */
    @Override
    public List<PostShortResponseDto> getLatestPosts() {
        return postRepository.findLatestPosts().stream()
                .map(post -> {
                    byte[] image = getPostImage(post.getId());

                    return new PostShortResponseDto(
                            post.getId(),
                            post.getContent(),
                            image,
                            0
                    );
                })
                .toList();
    }

    /**
     * Retrieves a specific post by its ID.
     *
     * @param postId   The ID of the post to retrieve.
     * @param pageable The Pageable object used for pagination.
     * @return The PostDetailsResponseDto representing the requested post.
     * @throws ResourceNotFoundException if the post with the given ID is not found.
     */
    @Override
    public PostDetailsResponseDto getPostById(UUID postId, Pageable pageable) {
        Post post = getPost(postId);
        byte[] postImage = getPostImage(postId);

        return new PostDetailsResponseDto(
                postId,
                post.getUser().getId(),
                post.getContent(),
                postImage,
                isCreator(getUserIdOrNull(), post),
                post.getCreatedAt(),
                post.getComments().size(),
                0
        );
    }

    /**
     * Removes a post by its ID.
     *
     * @param postId The ID of the post to remove.
     * @throws DeniedAccessException if the user does not have permission to remove the post.
     */
    @Override
    public void removePost(UUID postId) {
        UUID userId = getUserId();

        if (isCreator(userId, getPost(postId))) {
            if (getPost(postId).getImageLink() != null) {
                removePostImage(postId);
            }
            postRepository.deleteById(postId);
        } else {
            throw new DeniedAccessException("You don't have access, because you're not creator of this post!");
        }
    }

    /**
     * Creates a new post.
     *
     * @param postRequestDto The PostRequestDto containing the details of the new post.
     * @param file           The optional MultipartFile representing the post image.
     * @return The ID of the newly created post.
     * @throws ResourceNotFoundException if the user is not found when trying to create the post.
     */
    @Override
    public UUID createPost(PostRequestDto postRequestDto, MultipartFile file) {
        UUID userId = getUserId();

        Post post = postMapper.toPostEntity(postRequestDto);
        post.setUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found")));
        if (file != null && !file.isEmpty()) {
            String postImageId = UUID.randomUUID().toString();
            try {
                s3Service.putObject(
                        s3Buckets.getCustomer(),
                        "post-image/%s".formatted(postImageId),
                        file.getBytes()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            post.setImageLink(postImageId);
        }
        return postRepository.save(post).getId();
    }

    /**
     * Updates a post by its ID.
     *
     * @param postId         The ID of the post to update.
     * @param postRequestDto The PostRequestDto containing the updated post details.
     * @return The PostDetailsResponseDto representing the updated post.
     * @throws DeniedAccessException if the user is not the creator of the post and not authorized to update it.
     */
    @Override
    public PostDetailsResponseDto updatePost(UUID postId, PostRequestDto postRequestDto) {
        Post post = getPost(postId);
        UUID userId = getUserId();
        byte[] postImage = getPostImage(postId);

        if (!isCreator(userId, post)) {
            throw new DeniedAccessException("You don't have access, because you're not creator of this post!");
        }

        post.setContent(postRequestDto.content());
        postRepository.save(post);

        return new PostDetailsResponseDto(
                postId,
                post.getUser().getId(),
                post.getContent(),
                postImage,
                true,
                post.getCreatedAt(),
                post.getComments().size(),
                0
        );
    }

    /**
     * Retrieves all posts with pagination support.
     *
     * @param page The page number (0-indexed).
     * @param size The number of items per page.
     * @return A list of PostDetailsResponseDto representing the paginated posts.
     */
    @Override
    public List<PostDetailsResponseDto> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findAll(pageable);

        return postPage.getContent().stream()
                .map(post -> {
                    byte[] image = getPostImage(post.getId());
                    Integer commentCount = post.getComments().size();
                    return new PostDetailsResponseDto(
                            post.getId(),
                            post.getUser().getId(),
                            post.getContent(),
                            image,
                            isCreator(getUserIdOrNull(), post),
                            post.getCreatedAt(),
                            commentCount,
                            0
                    );
                }).toList();
    }

    /**
     * Retrieves the image of a post by its ID.
     *
     * @param postId The ID of the post.
     * @return The byte array representing the post image, or null if no image is found.
     * @throws ResourceNotFoundException if the post image with the given ID is not found.
     */
    @Override
    public byte[] getPostImage(UUID postId) {
        Post post = getPost(postId);
        String imageLink = post.getImageLink();

        if (imageLink == null) {
            return null;
        }

        if (imageLink.isBlank()) {
            throw new ResourceNotFoundException("post with id [%s] post image  not found".formatted(postId));
        }

        return s3Service.getObject(s3Buckets.getCustomer(),
                "post-image/%s".formatted(post.getImageLink())
        );
    }

    private void removePostImage(UUID postId) {
        Post post = getPost(postId);

        if (post.getImageLink().isBlank()) {
            throw new ResourceNotFoundException("post with id [%s] post image  not found".formatted(postId));
        }

        s3Service.deleteObject(s3Buckets.getCustomer(),
                "post-image/%s".formatted(post.getImageLink())
        );
    }

    private static UUID getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new DeniedAccessException("You can't do it before authenticate!");
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.user().getId();
    }

    private static UUID getUserIdOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.user().getId();
    }

    private static boolean isCreator(UUID userId, Post post) {
        if (userId == null) {
            return false;
        }
        return userId.equals(post.getUser().getId());
    }
}
