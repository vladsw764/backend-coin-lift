package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.comments.CommentRequestDto;
import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.services.comments.CommentService;
import com.coinlift.backend.services.users.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}")
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<UUID> createComment(@RequestBody CommentRequestDto commentRequestDto,
                                              @PathVariable("postId") UUID uuid,
                                              @RequestHeader("Authorization") String jwt) {
        UUID userId = jwtService.extractUserIdFromToken(jwt);
        return new ResponseEntity<>(commentService.createComment(commentRequestDto, uuid, userId), HttpStatus.CREATED);
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@RequestBody CommentRequestDto commentRequestDto,
                                                            @PathVariable("postId") UUID postId,
                                                            @PathVariable("commentId") UUID commentId,
                                                            @RequestHeader("Authorization") String jwt) {
        UUID userId = jwtService.extractUserIdFromToken(jwt);
        return new ResponseEntity<>(commentService.updateComment(commentRequestDto, postId, commentId, userId), HttpStatus.OK);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<String> removeComment(@PathVariable("postId") UUID postId,
                                                @PathVariable("commentId") UUID commentId,
                                                @RequestHeader("Authorization") String jwt) {
        UUID userId = jwtService.extractUserIdFromToken(jwt);
        commentService.deleteComment(postId, commentId, userId);
        return ResponseEntity.ok("Comment successfully deleted!");
    }
}
