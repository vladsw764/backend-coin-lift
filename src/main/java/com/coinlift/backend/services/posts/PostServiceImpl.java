package com.coinlift.backend.services.posts;

import com.coinlift.backend.config.s3.S3Buckets;
import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;
import com.coinlift.backend.entities.Comment;
import com.coinlift.backend.entities.MyUserDetails;
import com.coinlift.backend.entities.Post;
import com.coinlift.backend.exceptions.DeniedAccessException;
import com.coinlift.backend.exceptions.ResourceNotFoundException;
import com.coinlift.backend.mappers.CommentMapper;
import com.coinlift.backend.mappers.PostMapper;
import com.coinlift.backend.repositories.CommentRepository;
import com.coinlift.backend.repositories.PostRepository;
import com.coinlift.backend.repositories.UserRepository;
import com.coinlift.backend.services.s3.S3Service;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final UserRepository userRepository;

    private Post getPost(UUID postId) {
        return postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post with id [%s] not found".formatted(postId)));
    }

    @Override
    public List<PostResponseDto> getLatestPosts() {
        return postRepository.findLatestPosts().stream().map(postMapper::toPostResponseDto)
                .peek(post -> post.setImage(getPostImage(post.getUuid()))).toList();
    }

    @Override
    public PostDetailsResponseDto getPostById(UUID postId, Pageable pageable) {
        UUID userId;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
            userId = userDetails.user().getId();
        } else userId = null;

        Post post = getPost(postId);
        List<Comment> comments = commentRepository.findAllByPostId(postId, pageable);
        List<CommentResponseDto> commentResponseDtoList = comments.stream().map(commentMapper::toCommentResponseDto).toList();

        commentResponseDtoList.forEach(comment ->
                comment.setCommentCreator(userId != null && userId.equals(comment.getUserId()))
        );
        PostDetailsResponseDto postDto = postMapper.toPostDetailsResponseDto(post, commentResponseDtoList);
        postDto.setImage(getPostImage(postId));
        postDto.setCreator(userId != null && isCreator(userId, post));

        return postDto;
    }

    @Override
    public void removePost(UUID postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        UUID userId = userDetails.user().getId();

        if (isCreator(userId, getPost(postId))) {
            if (getPost(postId).getImageLink() != null) {
                removePostImage(postId);
            }
            postRepository.deleteById(postId);
        } else {
            throw new DeniedAccessException("You don't have access, because you're not creator of this post!");
        }
    }

    @Override
    public UUID createPost(PostRequestDto postRequestDto, MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        UUID userId = userDetails.user().getId();

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

    @Override
    public PostResponseDto updatePost(UUID postId, PostRequestDto postRequestDto) {
        Post post = getPost(postId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        UUID userId = userDetails.user().getId();

        if (!isCreator(userId, post)) {
            throw new DeniedAccessException("You don't have access, because you're not creator of this post!");
        }

        post.setTitle(postRequestDto.title());
        post.setContent(postRequestDto.content());
        postRepository.save(post);

        PostResponseDto postResponseDto = postMapper.toPostResponseDto(post);
        postResponseDto.setImage(getPostImage(postId));

        return postResponseDto;
    }

    @Override
    public List<PostResponseDto> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findAll(pageable);

        return postPage.getContent().stream().map(postMapper::toPostResponseDto)
                .peek(post -> post.setImage(getPostImage(post.getUuid()))).toList();
    }

    @Override
    public byte[] getPostImage(UUID postId) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post with id [%s] not found".formatted(postId)));

        if (post.getImageLink() == null) {
            return null;
        }

        if (post.getImageLink().isBlank()) {
            throw new ResourceNotFoundException("post with id [%s] post image  not found".formatted(postId));
        }

        return s3Service.getObject(s3Buckets.getCustomer(),
                "post-image/%s".formatted(post.getImageLink())
        );
    }

    public void removePostImage(UUID postId) {
        Post post = getPost(postId);

        if (post.getImageLink().isBlank()) {
            throw new ResourceNotFoundException("post with id [%s] post image  not found".formatted(postId));
        }

        s3Service.deleteObject(s3Buckets.getCustomer(),
                "post-image/%s".formatted(post.getImageLink())
        );
    }

    private static boolean isCreator(UUID userId, Post post) {
        return userId.equals(post.getUser().getId());
    }
}
