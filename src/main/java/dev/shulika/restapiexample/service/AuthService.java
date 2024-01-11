package dev.shulika.restapiexample.service;

import dev.shulika.restapiexample.dto.auth.JwtAuthResponseDto;
import dev.shulika.restapiexample.dto.auth.SignInRequestDto;
import dev.shulika.restapiexample.dto.auth.SignUpRequestDto;

public interface AuthService {

    JwtAuthResponseDto signUp(SignUpRequestDto request);

    JwtAuthResponseDto signIn(SignInRequestDto request);

}
