package dev.shulika.restapiexample.service.impl;

import dev.shulika.restapiexample.dto.auth.JwtAuthResponseDto;
import dev.shulika.restapiexample.dto.auth.SignInRequestDto;
import dev.shulika.restapiexample.dto.auth.SignUpRequestDto;
import dev.shulika.restapiexample.exception.AlreadyExistsException;
import dev.shulika.restapiexample.model.Person;
import dev.shulika.restapiexample.model.Role;
import dev.shulika.restapiexample.repository.PersonRepository;
import dev.shulika.restapiexample.service.AuthService;
import dev.shulika.restapiexample.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static dev.shulika.restapiexample.constant.ServiceConst.PERSON_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthResponseDto signUp(SignUpRequestDto request) {
        log.info("IN AuthServiceImpl - signUp() - STARTED");
        if (personRepository.existsByEmailAllIgnoreCase(request.getEmail())) {
            throw new AlreadyExistsException(PERSON_EXIST + request.getEmail());
        }

        Person person = Person.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        person = personRepository.save(person);
        String jwt = jwtService.generateToken(person.getEmail());
        return JwtAuthResponseDto.builder().token(jwt).build();
    }

    @Override
    public JwtAuthResponseDto signIn(SignInRequestDto request) {
        log.info("IN AuthServiceImpl - signIn() - STARTED");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String jwt = jwtService.generateToken(request.getEmail());
        return JwtAuthResponseDto.builder().token(jwt).build();
    }

}
