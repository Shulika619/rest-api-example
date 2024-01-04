package dev.shulika.restapiexample.mapper;

import dev.shulika.restapiexample.dto.PersonDto;
import dev.shulika.restapiexample.model.Person;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonMapper {
    Person toEntity(PersonDto personDto);

    PersonDto toDto(Person person);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Person partialUpdate(PersonDto personDto, @MappingTarget Person person);
}