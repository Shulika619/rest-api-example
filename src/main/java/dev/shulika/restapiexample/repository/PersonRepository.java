package dev.shulika.restapiexample.repository;

import dev.shulika.restapiexample.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByEmailAllIgnoreCase(String email);
    Optional<Person> findByEmail(String email);
}