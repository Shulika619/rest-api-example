package dev.shulika.restapiexample.service;

import dev.shulika.restapiexample.dto.PersonRequestDto;
import dev.shulika.restapiexample.dto.PersonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonService {

    Page<PersonResponseDto> findAll(Pageable pageable);

    PersonResponseDto findById(Long id);

    PersonResponseDto create(PersonRequestDto personRequestDto);

    PersonResponseDto updateById(Long id, PersonRequestDto personRequestDto);

    void deleteById(Long id);

}
