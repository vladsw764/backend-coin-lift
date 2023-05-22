package com.coinlift.backend.services.posts;

import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;
import com.coinlift.backend.entities.Comment;
import com.coinlift.backend.entities.Post;
import com.coinlift.backend.exceptions.ResourceNotFoundException;
import com.coinlift.backend.mappers.CommentMapper;
import com.coinlift.backend.mappers.PostMapper;
import com.coinlift.backend.repositories.CommentRepository;
import com.coinlift.backend.repositories.PostRepository;

import com.coinlift.backend.config.s3.S3Buckets;
import com.coinlift.backend.repositories.UserRepository;
import com.coinlift.backend.services.s3.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Post post = getPost(postId);
        List<Comment> comments = commentRepository.findAllByPostId(postId, pageable);
        List<CommentResponseDto> commentResponseDtos = comments.stream().map(commentMapper::toCommentResponseDto).toList();
        PostDetailsResponseDto postDto = postMapper.toPostDetailsResponseDto(post, commentResponseDtos);
        postDto.setImage(getPostImage(postId));
        return postDto;
    }


    @Override
    public void removePost(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("post with id [%s] not found".formatted(postId));
        }
        postRepository.deleteById(postId);
    }


    @Override
    public UUID createPost(PostRequestDto postRequestDto, MultipartFile file, UUID userId) {
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
        Post post = postMapper.toPostEntity(postRequestDto);
        post.setUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found")));
        post.setImageLink(postImageId);
        return postRepository.save(post).getId();
    }

    @Override
    public PostResponseDto updatePost(UUID postId, PostRequestDto postRequestDto) {
        Post post = getPost(postId);
        post.setTitle(postRequestDto.title());
        post.setContent(postRequestDto.content());
        postRepository.save(post);
        return postMapper.toPostResponseDto(post);
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

        if (post.getImageLink().isBlank()) {
            throw new ResourceNotFoundException("post with id [%s] post image  not found".formatted(postId));
        }

        return s3Service.getObject(s3Buckets.getCustomer(),
                "post-image/%s".formatted(post.getImageLink())
        );
    }

    @Override
    public String extractJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid JWT token");
    }
}
