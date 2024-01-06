package dev.shulika.restapiexample.mapper;

import dev.shulika.restapiexample.dto.task.TaskRequestDto;
import dev.shulika.restapiexample.dto.task.TaskResponseDto;
import dev.shulika.restapiexample.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    @Mapping(source = "authorId", target = "author.id")
    Task toEntity(TaskRequestDto taskRequestDto);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "executor.id", target = "executorId")
    TaskResponseDto toDto(Task task);

}