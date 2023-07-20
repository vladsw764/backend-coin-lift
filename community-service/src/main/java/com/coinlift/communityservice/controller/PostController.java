package com.coinlift.communityservice.controller;

import com.coinlift.communityservice.dto.posts.PostDetailsResponseDto;
import com.coinlift.communityservice.dto.posts.PostRequestDto;
import com.coinlift.communityservice.dto.posts.PostResponseDto;
import com.coinlift.communityservice.service.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@CrossOrigin("*")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size) {
        return new ResponseEntity<>(postService.getAllPosts(page, size), HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<PostResponseDto>> getLatestPosts() {
        return new ResponseEntity<>(postService.getLatestPosts(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@RequestParam String title,
                                        @RequestParam String content,
                                        @RequestParam(value = "file", required = false) MultipartFile postImage,
                                        @RequestAttribute(name = "User-Id", required = false) String id) {

        if (postImage != null && !postImage.isEmpty()) {
            if (!Objects.requireNonNull(postImage.getContentType()).startsWith("image/")) {
                return new ResponseEntity<>("Only image files are allowed!", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
        }
        PostRequestDto postRequestDto = new PostRequestDto(title, content);
        UUID userId = UUID.fromString(id);
        return new ResponseEntity<>(postService.createPost(postRequestDto, postImage, userId), HttpStatus.CREATED);
    }


    @GetMapping({"{uuid}"})
    public ResponseEntity<PostDetailsResponseDto> getPost(@PathVariable(name = "uuid") UUID uuid,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size,
                                                          @RequestAttribute(name = "User-Id", required = false) String id) {
        Pageable pageable = PageRequest.of(page, size);
        UUID userId = UUID.fromString(id);
        return new ResponseEntity<>(postService.getPostById(uuid, pageable, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> removePostById(@PathVariable(name = "uuid") UUID uuid,
                                                 @RequestAttribute(name = "User-Id", required = false) String id) {
        UUID userId = UUID.fromString(id);
        postService.removePost(uuid, userId);
        return ResponseEntity.ok("Post successfully removed!");
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<PostResponseDto> updatePostById(@PathVariable(name = "uuid") UUID uuid,
                                                          @RequestBody PostRequestDto postRequestDto,
                                                          @RequestAttribute(name = "User-Id", required = false) String id) {
        UUID userId = UUID.fromString(id);
        return new ResponseEntity<>(postService.updatePost(uuid, postRequestDto, userId), HttpStatus.OK);
    }
}
