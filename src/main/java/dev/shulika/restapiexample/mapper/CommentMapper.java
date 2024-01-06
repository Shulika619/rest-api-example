package dev.shulika.restapiexample.mapper;

import dev.shulika.restapiexample.dto.CommentRequestDto;
import dev.shulika.restapiexample.dto.CommentResponseDto;
import dev.shulika.restapiexample.model.Comment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    @Mapping(source = "taskId", target = "task.id")
    Comment toEntity(CommentRequestDto commentRequestDto);

    @Mapping(source = "task.id", target = "taskId")
    CommentResponseDto toDto(Comment comment);

}