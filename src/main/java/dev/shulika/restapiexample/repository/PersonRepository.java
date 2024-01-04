package dev.shulika.restapiexample.repository;

import dev.shulika.restapiexample.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}