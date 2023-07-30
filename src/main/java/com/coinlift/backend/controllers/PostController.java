package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostShortResponseDto;
import com.coinlift.backend.services.posts.PostService;
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
@RequestMapping("/api/v1/posts")
@CrossOrigin("*")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Get all posts with pagination support.
     *
     * @param page The page number (default: 0).
     * @param size The number of posts per page (default: 20).
     * @return A ResponseEntity containing a list of PostDetailsResponseDto objects and HttpStatus OK if successful.
     */
    @GetMapping
    public ResponseEntity<List<PostDetailsResponseDto>> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "20") int size) {
        return new ResponseEntity<>(postService.getAllPosts(page, size), HttpStatus.OK);
    }

    /**
     * Get the latest posts.
     *
     * @return A ResponseEntity containing a list of PostShortResponseDto objects and HttpStatus OK if successful.
     */
    @GetMapping("/latest")
    public ResponseEntity<List<PostShortResponseDto>> getLatestPosts() {
        return new ResponseEntity<>(postService.getLatestPosts(), HttpStatus.OK);
    }

    /**
     * Create a new post with an optional image.
     *
     * @param content   The content of the post.
     * @param postImage An optional image file to be attached to the post.
     * @return A ResponseEntity containing the UUID of the newly created post and HttpStatus CREATED if successful.
     * If the postImage is not a valid image file, it returns a ResponseEntity with an error message and
     * HttpStatus UNSUPPORTED_MEDIA_TYPE.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@RequestParam String content,
                                        @RequestParam(value = "file", required = false) MultipartFile postImage) {

        if (postImage != null && !postImage.isEmpty()) {
            if (!Objects.requireNonNull(postImage.getContentType()).startsWith("image/")) {
                return new ResponseEntity<>("Only image files are allowed!", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
        }
        PostRequestDto postRequestDto = new PostRequestDto(content);
        return new ResponseEntity<>(postService.createPost(postRequestDto, postImage), HttpStatus.CREATED);
    }

    /**
     * Get a post by its UUID with pagination support for comments.
     *
     * @param uuid The UUID of the post to retrieve.
     * @param page The page number for comments (default: 0).
     * @param size The number of comments per page (default: 5).
     * @return A ResponseEntity containing the PostDetailsResponseDto object for the specified post and HttpStatus OK if successful.
     */
    @GetMapping({"/{uuid}"})
    public ResponseEntity<PostDetailsResponseDto> getPost(@PathVariable(name = "uuid") UUID uuid,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(postService.getPostById(uuid, pageable), HttpStatus.OK);
    }

    /**
     * Remove a post by its UUID.
     *
     * @param uuid The UUID of the post to remove.
     * @return A ResponseEntity with a success message and HttpStatus OK if the post was removed successfully.
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> removePostById(@PathVariable(name = "uuid") UUID uuid) {
        postService.removePost(uuid);
        return ResponseEntity.ok("Post successfully removed!");
    }

    /**
     * Update a post by its UUID.
     *
     * @param uuid           The UUID of the post to update.
     * @param postRequestDto The updated content of the post.
     * @return A ResponseEntity containing the updated PostDetailsResponseDto object and HttpStatus OK if successful.
     */
    @PatchMapping("/{uuid}")
    public ResponseEntity<PostDetailsResponseDto> updatePostById(@PathVariable(name = "uuid") UUID uuid,
                                                                 @RequestBody PostRequestDto postRequestDto) {
        return new ResponseEntity<>(postService.updatePost(uuid, postRequestDto), HttpStatus.OK);
    }
}
