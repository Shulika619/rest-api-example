package dev.shulika.restapiexample.service;

import dev.shulika.restapiexample.dto.person.PersonRequestDto;
import dev.shulika.restapiexample.dto.person.PersonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface PersonService {

    UserDetailsService personDetailsService();

    Page<PersonResponseDto> findAll(Pageable pageable);

    PersonResponseDto findById(Long id);

    PersonResponseDto updateById(Long id, PersonRequestDto personRequestDto);

    void deleteById(Long id);

}
