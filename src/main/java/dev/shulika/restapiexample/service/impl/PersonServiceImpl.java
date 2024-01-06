package dev.shulika.restapiexample.service.impl;

import dev.shulika.restapiexample.dto.PersonRequestDto;
import dev.shulika.restapiexample.dto.PersonResponseDto;
import dev.shulika.restapiexample.exception.AlreadyExistsException;
import dev.shulika.restapiexample.exception.NotFoundException;
import dev.shulika.restapiexample.mapper.PersonMapper;
import dev.shulika.restapiexample.model.Person;
import dev.shulika.restapiexample.repository.PersonRepository;
import dev.shulika.restapiexample.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static dev.shulika.restapiexample.constant.ServiceConst.PERSON_EXIST;
import static dev.shulika.restapiexample.constant.ServiceConst.PERSON_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public Page<PersonResponseDto> findAll(Pageable pageable) {
        log.info("IN PersonServiceImpl - findAll() - STARTED");
        Page<Person> personPage = personRepository.findAll(pageable);
        return personPage.map(personMapper::toDto);
    }

    @Override
    public PersonResponseDto findById(Long id) {
        log.info("IN PersonServiceImpl - findById() - STARTED");
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PERSON_NOT_FOUND + id));
        return personMapper.toDto(person);
    }

    @Override
    public PersonResponseDto create(PersonRequestDto personRequestDto) {
        log.info("IN PersonServiceImpl - create() - STARTED");
        if (personRepository.existsByEmailAllIgnoreCase(personRequestDto.getEmail())) {
            throw new AlreadyExistsException(PERSON_EXIST + personRequestDto.getEmail());
        }
        Person result = personRepository.save(personMapper.toEntity(personRequestDto));
        return personMapper.toDto(result);
    }

    @Override
    public PersonResponseDto updateById(Long id, PersonRequestDto personRequestDto) {
        log.info("IN PersonServiceImpl - updateById() - STARTED");
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PERSON_NOT_FOUND + id));

        person.setFirstName(personRequestDto.getFirstName());
        person.setLastName(personRequestDto.getLastName());
        person.setEmail(personRequestDto.getEmail());
        person.setPassword(personRequestDto.getPassword());

        Person savedPerson = personRepository.save(person);
        return personMapper.toDto(savedPerson);
    }

    @Override
    public void deleteById(Long id) {
        log.info("IN PersonServiceImpl - deleteById() - STARTED");
        if (!personRepository.existsById(id)) {
            throw new NotFoundException(PERSON_NOT_FOUND + id);
        }
        personRepository.deleteById(id);
    }

}
