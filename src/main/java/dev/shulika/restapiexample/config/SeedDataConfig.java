package dev.shulika.restapiexample.config;

import dev.shulika.restapiexample.model.*;
import dev.shulika.restapiexample.repository.CommentRepository;
import dev.shulika.restapiexample.repository.PersonRepository;
import dev.shulika.restapiexample.repository.TaskRepository;
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
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
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
                    .build();
            Person test = Person.builder()
                    .firstName("Test")
                    .lastName("Test")
                    .email("test@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.USER)
                    .build();
            personRepository.saveAll(List.of(admin, test));
            log.info("+++ IN SeedDataConfig - created ADMIN and TEST person");
        }

        if (taskRepository.count() == 0) {
            Task task1 = Task.builder()
                    .title("Task1")
                    .description("Task1 description")
                    .status(Status.PENDING)
                    .priority(Priority.LOW)
                    .author(Person.builder().id(1L).build())
                    .build();
            Task task2 = Task.builder()
                    .title("Task2")
                    .description("Task2 description")
                    .status(Status.PROCESS)
                    .priority(Priority.MIDDLE)
                    .author(Person.builder().id(2L).build())
                    .build();
            taskRepository.saveAll(List.of(task1, task2));
            log.info("+++ IN SeedDataConfig - created Tasks");
        }

        if (commentRepository.count() == 0) {
            Comment comment1 = Comment.builder()
                    .text("comment1")
                    .author(Person.builder().id(1L).build())
                    .task(Task.builder().id(1L).build())
                    .build();
            Comment comment2 = Comment.builder()
                    .text("comment2")
                    .author(Person.builder().id(2L).build())
                    .task(Task.builder().id(2L).build())
                    .build();
            Comment comment3 = Comment.builder()
                    .text("comment3")
                    .author(Person.builder().id(2L).build())
                    .task(Task.builder().id(2L).build())
                    .build();
            commentRepository.saveAll(List.of(comment1, comment2,comment3));
            log.info("+++ IN SeedDataConfig - created Comments");
        }

    }

}