package dev.shulika.restapiexample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shulika.restapiexample.dto.auth.JwtAuthResponseDto;
import dev.shulika.restapiexample.dto.auth.SignInRequestDto;
import dev.shulika.restapiexample.dto.person.PersonRequestDto;
import dev.shulika.restapiexample.dto.person.PersonRequestRoleDto;
import dev.shulika.restapiexample.model.Role;
import dev.shulika.restapiexample.repository.PersonRepository;
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
class PersonControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PersonRepository personRepository;

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
        repoSizeBefore = personRepository.findAll().size();
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void testWithoutJWT() throws Exception {
        mockMvc.perform(get("/api/v1/persons")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/persons/1")).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/v1/persons/1")).andExpect(status().isForbidden());
        mockMvc.perform(put("/api/v1/persons/1/role")).andExpect(status().isForbidden());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/persons/1")).andExpect(status().isForbidden());
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get("/api/v1/persons")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(repoSizeBefore))
                .andExpect(jsonPath("$.content", Matchers.hasSize(repoSizeBefore)));
    }

    @Test
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/v1/persons/1")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("Admin"))
                .andExpect(jsonPath("$.lastName").value("Admin"))
                .andExpect(jsonPath("$.email").value("admin@gmail.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/persons/777")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Person not found! id: 777"));
    }

    @Test
    void testUpdate() throws Exception {
        PersonRequestDto personRequestDto = new PersonRequestDto("New", "New", "new@gmail.com", "123456");

        mockMvc.perform(put("/api/v1/persons/2")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(personRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value(personRequestDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(personRequestDto.getLastName()))
                .andExpect(jsonPath("$.email").value(personRequestDto.getEmail()));
    }

    @Test
    void testUpdateValidate() throws Exception {
        PersonRequestDto personRequestDto = new PersonRequestDto("", "", "", "");

        var result = mockMvc.perform(put("/api/v1/persons/2")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(personRequestDto)))
                .andExpect(status().isBadRequest()).andReturn();
        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("Password is mandatory");
        assertThat(result.getResponse().getContentAsString()).contains("Last name is mandatory");
        assertThat(result.getResponse().getContentAsString()).contains("First name is mandatory");
        assertThat(result.getResponse().getContentAsString()).contains("Email is mandatory");
    }

    @Test
    void testUpdateAccessDenied() throws Exception {
        PersonRequestDto personRequestDto = new PersonRequestDto("New", "New", "new@gmail.com", "123456");

        mockMvc.perform(put("/api/v1/persons/777").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(personRequestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value("Authorization Failure: Access denied, you dont have permission to access this resource"));
    }

    @Test
    void testUpdateRole() throws Exception {
        SignInRequestDto signInRequestDto = new SignInRequestDto("admin@gmail.com", "123456");
        var result = mockMvc.perform(post("/api/v1/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto)))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse()).isNotNull();
        var jwtAuthResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), JwtAuthResponseDto.class);
        assertThat(jwtAuthResponseDto.getToken()).isNotBlank();
        token = jwtAuthResponseDto.getToken();

        PersonRequestRoleDto personRequestRoleDto = new PersonRequestRoleDto(Role.ADMIN);
        mockMvc.perform(put("/api/v1/persons/2/role")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(personRequestRoleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.role").value(personRequestRoleDto.getRole().toString()));

        PersonRequestRoleDto personRequestRoleDto2 = new PersonRequestRoleDto(Role.USER);
        mockMvc.perform(put("/api/v1/persons/2/role")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(personRequestRoleDto2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.role").value(personRequestRoleDto2.getRole().toString()));
    }

    @Test
    void testUpdateRoleAccessDenied() throws Exception {
        PersonRequestRoleDto personRequestRoleDto = new PersonRequestRoleDto(Role.ADMIN);

        mockMvc.perform(put("/api/v1/persons/1/role")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(personRequestRoleDto)))
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

        mockMvc.perform(delete("/api/v1/persons/3")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertTrue(personRepository.findAll().size() < repoSizeBefore);
    }

    @Test
    void testDeleteAccessDenied() throws Exception {
        mockMvc.perform(delete("/api/v1/persons/1")
                        .header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value("Authorization Failure: Access denied, you dont have permission to access this resource"));
    }

}