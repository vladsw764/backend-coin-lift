package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.comments.CommentRequestDto;
import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.services.comments.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts/{postId}")
@CrossOrigin("*")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<UUID> createComment(@RequestBody CommentRequestDto commentRequestDto,
                                              @PathVariable("postId") UUID uuid) {
        return new ResponseEntity<>(commentService.createComment(commentRequestDto, uuid), HttpStatus.CREATED);
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@RequestBody CommentRequestDto commentRequestDto,
                                                            @PathVariable("postId") UUID postId,
                                                            @PathVariable("commentId") UUID commentId) {
        return new ResponseEntity<>(commentService.updateComment(commentRequestDto, postId, commentId), HttpStatus.OK);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<String> removeComment(@PathVariable("postId") UUID postId,
                                                @PathVariable("commentId") UUID commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.ok("Comment successfully deleted!");
    }
}
