package dev.shulika.restapiexample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shulika.restapiexample.dto.auth.JwtAuthResponseDto;
import dev.shulika.restapiexample.dto.auth.SignInRequestDto;
import dev.shulika.restapiexample.dto.task.TaskExecutorRequestDto;
import dev.shulika.restapiexample.dto.task.TaskRequestDto;
import dev.shulika.restapiexample.dto.task.TaskStatusRequestDto;
import dev.shulika.restapiexample.model.Priority;
import dev.shulika.restapiexample.model.Status;
import dev.shulika.restapiexample.repository.TaskRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TaskRepository taskRepository;

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
        repoSizeBefore = taskRepository.findAll().size();
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void testWithoutJWT() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/tasks/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/tasks/1/comments")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/tasks/authors/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/tasks/authors/1/comments")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/tasks/executors/1")).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/v1/tasks")).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/v1/tasks/1")).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/v1/tasks/1/status")).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/v1/tasks/1/executor")).andExpect(status().isForbidden());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/1")).andExpect(status().isForbidden());
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(repoSizeBefore))
                .andExpect(jsonPath("$.content", Matchers.hasSize(repoSizeBefore)));
    }

    @Test
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/1")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Task1"))
                .andExpect(jsonPath("$.description").value("Task1 description"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.authorId").value(1));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/777")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Task not found! id: 777"));
    }

    @Test
    void testGetByIdWithComments() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/1/comments")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Task1"))
                .andExpect(jsonPath("$.description").value("Task1 description"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.authorId").value(1))
                .andExpect(jsonPath("$.comments", Matchers.hasSize(1)));
    }

    @Test
    void testGetByIdWithCommentsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/777/comments")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Task not found! id: 777"));
    }

    @Test
    void testGetByAuthorId() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/authors/1")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)));
    }

    @Test
    void testGetByAuthorIdNotFoundOrNoTasks() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/authors/777")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.content", Matchers.hasSize(0)));
    }

    @Test
    void testGetByAuthorIdWithComments() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/authors/1/comments")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content.*.comments.*.id", Matchers.hasSize(1)));
    }

    @Test
    void testGetByAuthorIdWithCommentsNotFoundOrNoTasks() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/authors/777/comments")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.content", Matchers.hasSize(0)));
    }

    @Test
    void testGetByExecutorId() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/executors/3")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content.*.id").isNotEmpty());
    }

    @Test
    void testGetByExecutorIdNotFoundOrNoTasks() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/executors/777")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.content", Matchers.hasSize(0)));
    }

    @Test
    void testCreate() throws Exception {
        TaskRequestDto taskRequestDto = new TaskRequestDto("New task", "new", Status.PENDING, Priority.LOW, 3L);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(taskRequestDto.getTitle()))
                .andExpect(jsonPath("$.description").value(taskRequestDto.getDescription()))
                .andExpect(jsonPath("$.status").value(taskRequestDto.getStatus().toString()))
                .andExpect(jsonPath("$.priority").value(taskRequestDto.getPriority().toString()))
                .andExpect(jsonPath("$.authorId").value(taskRequestDto.getAuthorId()))
                .andExpect(jsonPath("$.executorId").isEmpty());
        assertTrue(taskRepository.findAll().size() > repoSizeBefore);
    }

    @Test
    void testCreateValidate() throws Exception {

        var result = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content("{ }"))
                .andExpect(status().isBadRequest()).andReturn();
        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("priority : Priority is mandatory");
        assertThat(result.getResponse().getContentAsString()).contains("title : Title is mandatory");
        assertThat(result.getResponse().getContentAsString()).contains("status : Status is mandatory");
        assertThat(result.getResponse().getContentAsString()).contains("description : Description is mandatory");
        assertThat(result.getResponse().getContentAsString()).contains("authorId : Author id is mandatory");
    }


    @Test
    void testUpdate() throws Exception {
        TaskRequestDto taskRequestDto = new TaskRequestDto("Updated task", "updated", Status.PROCESS, Priority.LOW, 2L);

        mockMvc.perform(put("/api/v1/tasks/2")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(taskRequestDto.getTitle()))
                .andExpect(jsonPath("$.description").value(taskRequestDto.getDescription()))
                .andExpect(jsonPath("$.status").value(taskRequestDto.getStatus().toString()))
                .andExpect(jsonPath("$.priority").value(taskRequestDto.getPriority().toString()))
                .andExpect(jsonPath("$.authorId").value(taskRequestDto.getAuthorId()));
    }

    @Test
    void testUpdateNotFound() throws Exception {
        TaskRequestDto taskRequestDto = new TaskRequestDto("Updated task", "updated", Status.PROCESS, Priority.LOW, 2L);

        mockMvc.perform(put("/api/v1/tasks/777")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Task not found! id: 777"));
    }

    @Test
    void testUpdateAccessDenied() throws Exception {
        TaskRequestDto taskRequestDto = new TaskRequestDto("Updated task", "updated", Status.PROCESS, Priority.LOW, 3L);

        mockMvc.perform(put("/api/v1/tasks/3")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value("Authorization Failure: Access denied, you dont have permission to access this resource"));
    }

    @Test
    void testUpdateStatus() throws Exception {
        TaskStatusRequestDto taskStatusRequestDto = new TaskStatusRequestDto(Status.COMPLETED);

        mockMvc.perform(put("/api/v1/tasks/2/status")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(taskStatusRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value(taskStatusRequestDto.getStatus().toString()));
    }

    @Test
    void testUpdateExecutor() throws Exception {
        TaskExecutorRequestDto taskExecutorRequestDto = new TaskExecutorRequestDto(2L);

        mockMvc.perform(put("/api/v1/tasks/2/executor")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(taskExecutorRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.executorId").value(taskExecutorRequestDto.getExecutorId()));
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

        mockMvc.perform(delete("/api/v1/tasks/3")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
        assertTrue(taskRepository.findAll().size() < repoSizeBefore);
    }

}