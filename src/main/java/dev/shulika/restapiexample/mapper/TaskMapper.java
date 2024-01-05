package dev.shulika.restapiexample.mapper;

import dev.shulika.restapiexample.dto.TaskDto;
import dev.shulika.restapiexample.model.Task;
import org.mapstruct.*;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "executorId", target = "executor.id")
    Task toEntity(TaskDto taskDto);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "executor.id", target = "executorId")
    TaskDto toDto(Task task);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "executorId", target = "executor.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task partialUpdate(TaskDto taskDto, @MappingTarget Task task);

}