package dev.shulika.restapiexample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shulika.restapiexample.dto.auth.JwtAuthResponseDto;
import dev.shulika.restapiexample.dto.auth.SignInRequestDto;
import dev.shulika.restapiexample.dto.auth.SignUpRequestDto;
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

import static dev.shulika.restapiexample.constant.ServiceConst.PERSON_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
//@Transactional
class AuthControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1-alpine");

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void testSignUp() throws Exception {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("User", "User", "user@gmail.com", "123456");

        var result = mockMvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andExpect(status().isCreated()).andReturn();

        assertThat(result.getResponse()).isNotNull();
        var jwtAuthResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), JwtAuthResponseDto.class);
        assertThat(jwtAuthResponseDto.getToken()).isNotNull();
    }

    @Test
    void testSignUpPersonAlreadyExist() throws Exception {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("User2", "User2", "user2@gmail.com", "123456");

        mockMvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andExpect(status().isCreated());

        var result = mockMvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).startsWith(PERSON_EXIST);
    }

    @Test
    void testSignUpValidate() throws Exception {
        var result = mockMvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ }"))
                .andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("Password is mandatory");
        assertThat(result.getResponse().getContentAsString()).contains("Last name is mandatory");
        assertThat(result.getResponse().getContentAsString()).contains("First name is mandatory");
        assertThat(result.getResponse().getContentAsString()).contains("Email is mandatory");
    }

    @Test
    void testSignUpValidateMinLength() throws Exception {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("Us", "Us", "use", "123");

        var result = mockMvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("Incorrect email format");
        assertThat(result.getResponse().getContentAsString()).contains("Email must be from");
        assertThat(result.getResponse().getContentAsString()).contains("First name must be from");
        assertThat(result.getResponse().getContentAsString()).contains("Password must be from");
        assertThat(result.getResponse().getContentAsString()).contains("Last name must be from");
    }

    @Test
    void testSignIn() throws Exception {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("User5", "User5", "user5@gmail.com", "123456");

        mockMvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andExpect(status().isCreated());

        SignInRequestDto signInRequestDto = new SignInRequestDto("user5@gmail.com", "123456");

        var result = mockMvc.perform(post("/api/v1/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto)))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse()).isNotNull();
        var jwtAuthResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), JwtAuthResponseDto.class);
        assertThat(jwtAuthResponseDto.getToken()).isNotBlank();
    }

    @Test
    void testSignInBadCredentials() throws Exception {
        SignInRequestDto signInRequestDto = new SignInRequestDto("user777@gmail.com", "123456");
        var result = mockMvc.perform(post("/api/v1/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto)))
                .andExpect(status().isUnauthorized()).andReturn();

        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("Bad Credentials, check email/password");
    }

    @Test
    void testSignInValidate() throws Exception {
        var result = mockMvc.perform(post("/api/v1/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ }"))
                .andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("Email is mandatory");
        assertThat(result.getResponse().getContentAsString()).contains("Password is mandatory");
    }

    @Test
    void testSignInValidateMinLength() throws Exception {
        SignInRequestDto signInRequestDto = new SignInRequestDto("us", "12");

        var result = mockMvc.perform(post("/api/v1/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto)))
                .andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResponse()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).contains("Email must be from");
        assertThat(result.getResponse().getContentAsString()).contains("Incorrect email format");
        assertThat(result.getResponse().getContentAsString()).contains(" Password must be from");
    }

}