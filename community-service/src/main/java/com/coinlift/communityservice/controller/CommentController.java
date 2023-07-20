package com.coinlift.communityservice.controller;

import com.coinlift.communityservice.dto.comments.CommentRequestDto;
import com.coinlift.communityservice.dto.comments.CommentResponseDto;
import com.coinlift.communityservice.service.comments.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}")
@CrossOrigin("*")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<UUID> createComment(@RequestBody CommentRequestDto commentRequestDto,
                                              @PathVariable("postId") UUID uuid,
                                              @RequestAttribute(name = "User-Id", required = false) String id) {
        UUID userId = UUID.fromString(id);
        return new ResponseEntity<>(commentService.createComment(commentRequestDto, uuid, userId), HttpStatus.CREATED);
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@RequestBody CommentRequestDto commentRequestDto,
                                                            @PathVariable("postId") UUID postId,
                                                            @PathVariable("commentId") UUID commentId,
                                                            @RequestAttribute(name = "User-Id", required = false) String id) {
        UUID userId = UUID.fromString(id);
        return new ResponseEntity<>(commentService.updateComment(commentRequestDto, postId, commentId, userId), HttpStatus.OK);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<String> removeComment(@PathVariable("postId") UUID postId,
                                                @PathVariable("commentId") UUID commentId,
                                                @RequestAttribute(name = "User-Id", required = false) String id) {
        UUID userId = UUID.fromString(id);
        commentService.deleteComment(postId, commentId, userId);
        return ResponseEntity.ok("Comment successfully deleted!");
    }
}
