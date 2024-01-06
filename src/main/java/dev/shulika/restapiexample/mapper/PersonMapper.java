package dev.shulika.restapiexample.mapper;

import dev.shulika.restapiexample.dto.PersonRequestDto;
import dev.shulika.restapiexample.dto.PersonResponseDto;
import dev.shulika.restapiexample.model.Person;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonMapper {
    Person toEntity(PersonRequestDto personRequestDto);

    PersonResponseDto toDto(Person person);

}