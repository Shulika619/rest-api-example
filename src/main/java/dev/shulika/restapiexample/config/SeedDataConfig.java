package dev.shulika.restapiexample.config;

import dev.shulika.restapiexample.model.Person;
import dev.shulika.restapiexample.model.Role;
import dev.shulika.restapiexample.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (personRepository.count() == 0) {

            Person admin = Person.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.ADMIN)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Person test = Person.builder()
                    .firstName("Test")
                    .lastName("Test")
                    .email("test@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.USER)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            personRepository.saveAll(List.of(admin, test));
            log.info("+++ IN SeedDataConfig - created ADMIN and TEST person");
        }
    }

}