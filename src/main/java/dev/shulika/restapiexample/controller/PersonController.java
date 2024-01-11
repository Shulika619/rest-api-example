package dev.shulika.restapiexample.controller;

import dev.shulika.restapiexample.dto.person.PersonRequestDto;
import dev.shulika.restapiexample.dto.person.PersonResponseDto;
import dev.shulika.restapiexample.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/persons")
@Tag(name = "Person", description = "Person management APIs")
@SecurityRequirement(name = "bearerAuth")
public class PersonController {

    private final PersonService personService;

    @Operation(summary = "Get persons", description = "Provides all persons, supports pagination and filtering")
    @GetMapping
    public Page<PersonResponseDto> getAll(@ParameterObject Pageable pageable) {
        return personService.findAll(pageable);
    }

    @Operation(summary = "Get person", description = "Provides person by id")
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> getById(@Valid @PathVariable Long id) {
        PersonResponseDto personRequestDto = personService.findById(id);
        return new ResponseEntity<>(personRequestDto, HttpStatus.OK);
    }

    @Operation(summary = "Update person",
            description = "Only the person himself or a person with the ADMIN role can update a user by ID")
    @PreAuthorize("#id == authentication.principal.id or hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDto> update(
            @Valid @PathVariable Long id,
            @Valid @RequestBody PersonRequestDto personRequestDto
    ) {
        PersonResponseDto personResponseDto = personService.updateById(id, personRequestDto);
        return new ResponseEntity<>(personResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete person",
            description = "Only the person himself or a person with the ADMIN role can delete a user by ID")
    @PreAuthorize("#id == authentication.principal.id or hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @PathVariable Long id) {
        personService.deleteById(id);
    }

}
