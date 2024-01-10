package dev.shulika.restapiexample.controller;

import dev.shulika.restapiexample.dto.auth.JwtAuthResponseDto;
import dev.shulika.restapiexample.dto.auth.SignInRequestDto;
import dev.shulika.restapiexample.dto.auth.SignUpRequestDto;
import dev.shulika.restapiexample.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth management APIs")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registration user", description = "Registering a new user and receiving a token")
    @PostMapping("/signup")
    public JwtAuthResponseDto signup(@Valid @RequestBody SignUpRequestDto request) {
        return authService.signUp(request);
    }

    @Operation(summary = "Login user",
            description = "Authenticate an existing user by email and password and receive a token")
    @PostMapping("/signin")
    public JwtAuthResponseDto signin(@Valid @RequestBody SignInRequestDto request) {
        return authService.signIn(request);
    }

}
