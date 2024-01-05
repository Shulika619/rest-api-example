package dev.shulika.restapiexample.controller;

import dev.shulika.restapiexample.dto.PersonDto;
import dev.shulika.restapiexample.model.Person;
import dev.shulika.restapiexample.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public Page<PersonDto> getAll(Pageable pageable) {
        return personService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getById(@Valid @PathVariable Long id) {
        PersonDto personDto = personService.findById(id);
        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody PersonDto personDto) {
        Person person = personService.create(personDto);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> update(@Valid @PathVariable Long id, @Valid @RequestBody PersonDto personDto) {
        Person person = personService.updateById(id, personDto);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@Valid @PathVariable Long id) {
        personService.deleteById(id);
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }

}
