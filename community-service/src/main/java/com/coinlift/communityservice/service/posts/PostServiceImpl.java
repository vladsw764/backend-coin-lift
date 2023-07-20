package com.coinlift.communityservice.service.posts;

import com.coinlift.communityservice.dto.comments.CommentResponseDto;
import com.coinlift.communityservice.dto.posts.PostDetailsResponseDto;
import com.coinlift.communityservice.dto.posts.PostRequestDto;
import com.coinlift.communityservice.dto.posts.PostResponseDto;
import com.coinlift.communityservice.model.Comment;
import com.coinlift.communityservice.model.Post;
import com.coinlift.communityservice.exception.DeniedAccessException;
import com.coinlift.communityservice.exception.ResourceNotFoundException;
import com.coinlift.communityservice.mapper.CommentMapper;
import com.coinlift.communityservice.mapper.PostMapper;
import com.coinlift.communityservice.repository.CommentRepository;
import com.coinlift.communityservice.repository.PostRepository;
import com.coinlift.communityservice.service.users.UserRestService;
import com.coinlift.userservice.dto.UserResponseDto;
import com.coinlift.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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
    private final WebClient webClient;
    private final UserRestService userRestService;

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
    public PostDetailsResponseDto getPostById(UUID postId, Pageable pageable, UUID userId) {
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
    public void removePost(UUID postId, UUID userId) {
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
    public UUID createPost(PostRequestDto postRequestDto, MultipartFile file, UUID userId) {
        Post post = postMapper.toPostEntity(postRequestDto);
        post.setUser(toEntity(userRestService.getUserById(userId)));
        if (file != null && !file.isEmpty()) {
            String postImageId = UUID.randomUUID().toString();

            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("key", "post-image/%s".formatted(postImageId));
            try {
                formData.add("file", new ByteArrayResource(file.getBytes()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            webClient.post()
                    .uri("http://localhost:8083/api/s3/upload")
                    .body(BodyInserters.fromMultipartData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            post.setImageLink(postImageId);
        }
        return postRepository.save(post).getId();
    }

    @Override
    public PostResponseDto updatePost(UUID postId, PostRequestDto postRequestDto, UUID userId) {
        Post post = getPost(postId);

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

        return webClient.get()
                .uri("http://localhost:8083/api/s3/download?key={key}", "post-image/%s".formatted(post.getImageLink()))
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

    public void removePostImage(UUID postId) {
        Post post = getPost(postId);

        if (post.getImageLink().isBlank()) {
            throw new ResourceNotFoundException("post with id [%s] post image  not found".formatted(postId));
        }


        webClient.delete()
                .uri("http://localhost:8083/api/s3/delete?key={key}", "post-image/%s".formatted(post.getImageLink()))
                .retrieve()
                .bodyToMono(Void.class)
                .block();

    }

    private static boolean isCreator(UUID userId, Post post) {
        return userId.equals(post.getUser().getId());
    }

    User toEntity(UserResponseDto userResponseDto) {
        return User.builder()
                .id(userResponseDto.id())
                .email(userResponseDto.emailAddress())
                .username(userResponseDto.username())
                .build();
    }
}
