package dev.shulika.restapiexample.mapper;

import dev.shulika.restapiexample.dto.CommentDto;
import dev.shulika.restapiexample.model.Comment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    @Mapping(source = "taskId", target = "task.id")
    Comment toEntity(CommentDto commentDto);

    @Mapping(source = "task.id", target = "taskId")
    CommentDto toDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment partialUpdate(CommentDto commentDto, @MappingTarget Comment comment);

}