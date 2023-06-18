package ru.practicum.shareit.item.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;


@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface CommetMapper {

    @Mapping(target = "authorName", source = "comment.author.name")
    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "author.name", source = "comment.authorName")
    Comment toCommentModel(CommentDto comment);

    List<CommentDto> toCommentListDto(List<Comment> comment);

}
