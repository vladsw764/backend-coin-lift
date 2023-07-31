package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.comments.CommentRequestDto;
import com.coinlift.backend.dtos.comments.CommentResponseDto;
import com.coinlift.backend.services.comments.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private CommentService commentService;


    private CommentRequestDto commentRequestDto;

    @BeforeEach
    void setUp() {
        commentRequestDto = new CommentRequestDto("test content");
    }

    @Test
    @WithMockUser
    public void createComment_returnsCommentId() throws Exception {
        UUID postId = UUID.randomUUID();

        UUID commentId = UUID.randomUUID();
        when(commentService.createComment(commentRequestDto, postId)).thenReturn(commentId);

        mockMvc.perform(post("/api/v1/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(commentRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", is(commentId.toString())))
                .andDo(print());

        verify(commentService, times(1)).createComment(commentRequestDto, postId);
    }


    @Test
    @WithMockUser
    void updateComment_returnsCommentResponseDto() throws Exception {
        UUID postId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        CommentResponseDto commentResponseDto = new CommentResponseDto(commentId, userId, "test content", LocalDateTime.now(), true);

        when(commentService.updateComment(commentRequestDto, postId, commentId)).thenReturn(commentResponseDto);

        mockMvc.perform(patch("/api/v1/posts/{postId}/{commentId}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(commentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentId.toString())))
                .andExpect(jsonPath("$.content", is("test content")))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void removeComment() throws Exception {
        UUID postId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/posts/{postId}/{commentId}", postId, commentId))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment successfully deleted!"))
                .andDo(print());

    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}