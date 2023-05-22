package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;
import com.coinlift.backend.services.posts.PostService;
import com.coinlift.backend.services.users.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@CrossOrigin("*")
public class PostController {
    private final PostService postService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "30") int size) {
        return new ResponseEntity<>(postService.getAllPosts(page, size), HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<PostResponseDto>> getLatestPosts() {
        return new ResponseEntity<>(postService.getLatestPosts(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UUID> createPost(@RequestParam String title,
                                           @RequestParam String content,
                                           @RequestParam(value = "file") MultipartFile postImage,
                                           @RequestHeader("Authorization") String jwt) {
        PostRequestDto postRequestDto = new PostRequestDto(title, content);
        UUID userId = jwtService.extractUserIdFromToken(jwt);
        return new ResponseEntity<>(postService.createPost(postRequestDto, postImage, userId), HttpStatus.CREATED);
    }


    @GetMapping({"{uuid}"})
    public ResponseEntity<PostDetailsResponseDto> getPost(@PathVariable(name = "uuid") UUID uuid,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(postService.getPostById(uuid, pageable), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> removePostById(@PathVariable(name = "uuid") UUID uuid) {
        postService.removePost(uuid);
        return ResponseEntity.ok("Post successfully removed!");
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<PostResponseDto> updatePostById(@PathVariable(name = "uuid") UUID uuid,
                                                          @RequestBody PostRequestDto postRequestDto) {
        return new ResponseEntity<>(postService.updatePost(uuid, postRequestDto), HttpStatus.OK);
    }
}
