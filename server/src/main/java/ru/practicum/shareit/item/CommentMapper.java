package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface CommentMapper {
    CommentDto toCommentDto(Comment comment);

    Comment toCommentOnCreate(CommentCreateDto commentCreateDto, Item item, User author);

    List<CommentDto> toCommentDtoList(List<Comment> comments);
}
