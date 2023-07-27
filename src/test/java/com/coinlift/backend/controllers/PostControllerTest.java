package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.posts.PostDetailsResponseDto;
import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;
import com.coinlift.backend.dtos.posts.PostShortResponseDto;
import com.coinlift.backend.services.posts.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    PostService postService;

    private List<PostShortResponseDto> postShortResponseDtoList;
    private List<PostResponseDto> postResponseDtoList;


    @BeforeEach
    void setUp() {
        postShortResponseDtoList = Arrays.asList(
                new PostShortResponseDto(UUID.randomUUID(), "content_1", new byte[0], 2),
                new PostShortResponseDto(UUID.randomUUID(), "content_2", new byte[0], 34)
        );

        postResponseDtoList = Arrays.asList(
                new PostResponseDto(UUID.randomUUID(), "username_1", UUID.randomUUID(), "content_1",
                        new byte[0], 2, LocalDateTime.now(), false),
                new PostResponseDto(UUID.randomUUID(), "username_2", UUID.randomUUID(), "content_2",
                        new byte[0], 5, LocalDateTime.now(), true)
        );
    }


    @Test
    @DisplayName("GET api/v1/posts/latest")
    void getLatestPosts() throws Exception {
        when(postService.getLatestPosts()).thenReturn(postShortResponseDtoList);

        mockMvc.perform(get("/api/v1/posts/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(postShortResponseDtoList.size()))
                .andDo(print());
    }

    @Test
    @DisplayName("GET api/v1/posts")
    void getAllPosts_returnsListOfPosts() throws Exception {
        int page = 0;
        int size = 20;
        when(postService.getAllPosts(page, size)).thenReturn(postResponseDtoList);
        mockMvc.perform(get("/api/v1/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(postResponseDtoList.size()))
                .andDo(print());
    }

    @Test
    @DisplayName("GET api/v1/posts/{postId}")
    void getPostById_returnsPost() throws Exception {
        UUID uuid = UUID.randomUUID();

        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        PostDetailsResponseDto responseDto = new PostDetailsResponseDto(
                uuid, "username", UUID.randomUUID(), "content",
                new byte[0], LocalDateTime.now(), new ArrayList<>(), true, false
        );

        when(postService.getPostById(uuid, pageable)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/posts/{uuid}", uuid)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(uuid.toString()))
                .andExpect(jsonPath("$.username").value("username"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("POST api/v1/posts")
    void createPost_withValidData_returnsCreatedStatus() throws Exception {

        UUID postId = UUID.randomUUID();

        when(postService.createPost(any(PostRequestDto.class), any(MultipartFile.class))).thenReturn(postId);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                new byte[0]
        );

        mockMvc.perform(multipart("/api/v1/posts")
                        .file(multipartFile)
                        .param("content", "Test Content")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", is(postId.toString())))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE api/v1/posts/{postId}")
    void removePostById() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/{uuid}", UUID.randomUUID()))
                .andExpect(content().string("Post successfully removed!"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("PATCH api/v1/posts/{postId}")
    void updatePostById_returnsUpdatedPost() throws Exception {
        UUID postId = UUID.randomUUID();

        PostRequestDto postRequestDto = new PostRequestDto("test content");
        PostResponseDto postResponseDto = new PostResponseDto(postId, "user_1", UUID.randomUUID(), "test content", new byte[0], 1, LocalDateTime.now(), false);


        when(postService.updatePost(eq(postId), any(PostRequestDto.class))).thenReturn(postResponseDto);

        mockMvc.perform(patch("/api/v1/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(postRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", is(postId.toString())))
                .andExpect(jsonPath("$.content", is("test content")))
                .andExpect(jsonPath("$.username", is("user_1")))
                .andExpect(jsonPath("$.commentCount", is(1)))
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