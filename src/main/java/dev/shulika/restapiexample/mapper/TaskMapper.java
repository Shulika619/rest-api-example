package dev.shulika.restapiexample.mapper;

import dev.shulika.restapiexample.dto.task.TaskRequestDto;
import dev.shulika.restapiexample.dto.task.TaskResponseDto;
import dev.shulika.restapiexample.dto.task.TaskWithCommentDto;
import dev.shulika.restapiexample.model.Task;
import org.mapstruct.*;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    @Mapping(source = "authorId", target = "author.id")
    Task toEntity(TaskRequestDto taskRequestDto);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "executor.id", target = "executorId")
    TaskResponseDto toDto(Task task);

    @AfterMapping
    default void linkComments(@MappingTarget Task task) {
        task.getComments().forEach(comment -> comment.setTask(task));
    }

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "executor.id", target = "executorId")
    TaskWithCommentDto toDtoWithComment(Task task);

}