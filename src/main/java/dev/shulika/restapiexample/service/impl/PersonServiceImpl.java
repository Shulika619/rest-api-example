package dev.shulika.restapiexample.service.impl;

import dev.shulika.restapiexample.dto.person.PersonRequestDto;
import dev.shulika.restapiexample.dto.person.PersonResponseDto;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static dev.shulika.restapiexample.constant.ServiceConst.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public UserDetailsService personDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return personRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException(PERSON_NOT_FOUND_EMAIL));
            }
        };
    }

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
