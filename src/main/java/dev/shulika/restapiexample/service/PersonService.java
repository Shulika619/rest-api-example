package dev.shulika.restapiexample.service;

import dev.shulika.restapiexample.dto.PersonDto;
import dev.shulika.restapiexample.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonService {

    Page<PersonDto> findAll(Pageable pageable);

    PersonDto findById(Long id);

    Person create(PersonDto personDto);

    Person updateById(Long id, PersonDto personDto);

    void deleteById(Long id);

}
