package dev.shulika.restapiexample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shulika.restapiexample.dto.auth.JwtAuthResponseDto;
import dev.shulika.restapiexample.dto.auth.SignInRequestDto;
import dev.shulika.restapiexample.dto.comment.CommentRequestDto;
import dev.shulika.restapiexample.repository.CommentRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    CommentRepository commentRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1-alpine");

    String token;

    int repoSizeBefore;

    @BeforeEach
    void getToken() throws Exception {
        SignInRequestDto signInRequestDto = new SignInRequestDto("test@gmail.com", "123456");

        var result = mockMvc.perform(post("/api/v1/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto)))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse()).isNotNull();
        var jwtAuthResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), JwtAuthResponseDto.class);
        assertThat(jwtAuthResponseDto.getToken()).isNotBlank();
        token = jwtAuthResponseDto.getToken();
        repoSizeBefore = commentRepository.findAll().size();
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void testWithoutJWT() throws Exception {
        mockMvc.perform(get("/api/v1/comments")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/comments/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/comments/tasks/1")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/v1/comments")).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/v1/comments/1")).andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/v1/comments/1")).andExpect(status().isForbidden());
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get("/api/v1/comments")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(repoSizeBefore))
                .andExpect(jsonPath("$.content", Matchers.hasSize(repoSizeBefore)));
    }

    @Test
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/v1/comments/1")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.text").value("comment1"))
                .andExpect(jsonPath("$.authorId").value(1))
                .andExpect(jsonPath("$.taskId").value(1));
    }

    @Test
    void testGetByIdNotFoundComment() throws Exception {
        mockMvc.perform(get("/api/v1/comments/777")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Comment not found! id: 777"));
    }

    @Test
    void testGetByTaskId() throws Exception {
        mockMvc.perform(get("/api/v1/comments/tasks/1")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content.*.id").isNotEmpty())
                .andExpect(jsonPath("$.content.*.text").isNotEmpty())
                .andExpect(jsonPath("$.content.*.authorId").isNotEmpty())
                .andExpect(jsonPath("$.content.*.taskId").isNotEmpty());
    }

    @Test
    void testGetByTaskIdNotFoundTaskId() throws Exception {
        mockMvc.perform(get("/api/v1/comments/tasks/777")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void testCreate() throws Exception {
        CommentRequestDto commentRequestDto = new CommentRequestDto("new comment", 1L, 1L);

        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.text").value(commentRequestDto.getText()))
                .andExpect(jsonPath("$.authorId").value(commentRequestDto.getAuthorId()))
                .andExpect(jsonPath("$.taskId").value(commentRequestDto.getTaskId()));

        assertTrue(commentRepository.findAll().size() > repoSizeBefore);
    }

    @Test
    void testUpdate() throws Exception {
        CommentRequestDto commentRequestDto = new CommentRequestDto("updated comment", 2L, 2L);

        mockMvc.perform(put("/api/v1/comments/2")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.text").value(commentRequestDto.getText()))
                .andExpect(jsonPath("$.authorId").value(commentRequestDto.getAuthorId()))
                .andExpect(jsonPath("$.taskId").value(commentRequestDto.getTaskId()));
    }

    @Test
    void testUpdateAccessDenied() throws Exception {
        CommentRequestDto commentRequestDto = new CommentRequestDto("try comment", 1L, 1L);

        mockMvc.perform(put("/api/v1/comments/2")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value("Authorization Failure: Access denied, you dont have permission to access this resource"));
    }

    @Test
    void testDelete() throws Exception {
        SignInRequestDto signInRequestDto = new SignInRequestDto("admin@gmail.com", "123456");

        var result = mockMvc.perform(post("/api/v1/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto)))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse()).isNotNull();
        var jwtAuthResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), JwtAuthResponseDto.class);
        assertThat(jwtAuthResponseDto.getToken()).isNotBlank();
        token = jwtAuthResponseDto.getToken();

        int repoSizeBeforeDelete = repoSizeBefore;
        mockMvc.perform(delete("/api/v1/comments/3")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
        assertTrue(commentRepository.findAll().size() < repoSizeBeforeDelete);
    }

    @Test
    void testDeleteAccessDenied() throws Exception {
        mockMvc.perform(delete("/api/v1/comments/3")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value("Authorization Failure: Access denied, you dont have permission to access this resource"));
    }

}