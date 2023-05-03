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

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    private Post getPost(UUID postId) {
        return postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post with id" + postId + "not found"));
    }

    @Override
    public List<PostResponseDto> getLatestPosts() {
        return postRepository.findLatestPosts().stream().map(postMapper::toPostResponseDto).toList();
    }

    @Override
    public PostDetailsResponseDto getPostById(UUID postId, Pageable pageable) {
        Post post = getPost(postId);
        List<Comment> comments = commentRepository.findAllByPostId(postId, pageable);
        List<CommentResponseDto> commentResponseDtos = comments.stream().map(commentMapper::toCommentResponseDto).toList();
        return postMapper.toPostDetailsResponseDto(post, commentResponseDtos);
    }


    @Override
    public void removePost(UUID postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public UUID createPost(PostRequestDto postRequestDto) {
        Post post = Post.builder()
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .imageLink(postRequestDto.imageLink())
                .build();
        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Override
    public PostResponseDto updatePost(UUID postId, PostRequestDto postRequestDto) {
        Post post = postMapper.toPostEntity(postRequestDto);
        postRepository.save(post);
        return postMapper.toPostResponseDto(post);
    }

    @Override
    public List<PostResponseDto> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.getContent().stream().map(postMapper::toPostResponseDto).toList();
    }
}
