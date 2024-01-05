package dev.shulika.restapiexample.service.impl;

import dev.shulika.restapiexample.dto.PersonDto;
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
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    
    @Override
    public Page<PersonDto> findAll(Pageable pageable) {
        log.info("IN PersonServiceImpl - findAll() - STARTED");
        Page<Person> personPage = personRepository.findAll(pageable);
        return personPage.map(personMapper::toDto);
    }

    @Override
    public PersonDto findById(Long id) {
        log.info("IN PersonServiceImpl - findById() - STARTED");
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Person not found! id: " + id));
        return personMapper.toDto(person);
    }

    @Override
    @Transactional(readOnly = false)
    public Person create(PersonDto personDto) {
        log.info("IN PersonServiceImpl - create() - STARTED");
        if (personRepository.existsByEmailAllIgnoreCase(personDto.getEmail())) {
            throw new AlreadyExistsException("Person already exist! email: " + personDto.getEmail());
        }
        return personRepository.save(personMapper.toEntity(personDto));
    }

    @Override
    @Transactional(readOnly = false)
    public Person updateById(Long id, PersonDto personDto) {
        log.info("IN PersonServiceImpl - updateById() - STARTED");
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Person not found! id: " + id));
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setEmail(personDto.getEmail());
        person.setPassword(personDto.getPassword());
        return personRepository.save(person);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteById(Long id) {
        log.info("IN PersonServiceImpl - deleteById() - STARTED");
        if (!personRepository.existsById(id)) {
            throw new NotFoundException("Person not found! id: " + id);
        }
        personRepository.deleteById(id);
    }
}
