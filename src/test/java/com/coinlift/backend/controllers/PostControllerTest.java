package com.coinlift.backend.controllers;

import com.coinlift.backend.dtos.posts.PostRequestDto;
import com.coinlift.backend.dtos.posts.PostResponseDto;
import com.coinlift.backend.services.posts.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    PostService postService;

    private List<PostResponseDto> postResponseDtoList;

    @BeforeEach
    void setUp() {
        postResponseDtoList = Arrays.asList(
                new PostResponseDto(UUID.randomUUID(), "user_1", "title_1", "content_1", new byte[0], 2, LocalDateTime.now()),
                new PostResponseDto(UUID.randomUUID(), "user_2", "title_1", "content_2", new byte[0], 34, LocalDateTime.now())
        );
    }


    @Test
    @DisplayName("GET api/v1/posts/latest")
    void getLatestPosts() throws Exception {
        when(postService.getLatestPosts()).thenReturn(postResponseDtoList);

        mockMvc.perform(get("/api/v1/posts/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(postResponseDtoList.size()))
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
                        .param("title", "Test Title")
                        .param("content", "Test Content")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", is(postId.toString())))
                .andDo(print());
    }
}