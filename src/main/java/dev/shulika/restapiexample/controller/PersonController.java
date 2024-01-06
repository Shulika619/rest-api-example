package dev.shulika.restapiexample.controller;

import dev.shulika.restapiexample.dto.person.PersonRequestDto;
import dev.shulika.restapiexample.dto.person.PersonResponseDto;
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
    public Page<PersonResponseDto> getAll(Pageable pageable) {
        return personService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> getById(@Valid @PathVariable Long id) {
        PersonResponseDto personRequestDto = personService.findById(id);
        return new ResponseEntity<>(personRequestDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PersonResponseDto> create(@Valid @RequestBody PersonRequestDto personRequestDto) {
        PersonResponseDto personResponseDto = personService.create(personRequestDto);
        return new ResponseEntity<>(personResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDto> update(
            @Valid @PathVariable Long id,
            @Valid @RequestBody PersonRequestDto personRequestDto
    ) {
        PersonResponseDto personResponseDto = personService.updateById(id, personRequestDto);
        return new ResponseEntity<>(personResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @PathVariable Long id) {
        personService.deleteById(id);
    }

}
