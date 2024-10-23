package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Преобразователь данных ввода-вывода по отзывам.
 */

@Component
public class CommentMapperImpl implements CommentMapper {

    /**
     * Метод преобразования выходных данных по отзывам.
     *
     * @param comment отзыв для вывода.
     * @return Объект для вывода данных.
     */
    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItem().getId())
                .authorName(comment.getAuthor().getName())
                .build();
    }

    /**
     * Метод использования входных данных при добавлении отзыва.
     *
     * @param commentCreateDto объект с данными для создания;
     * @param item             вещь, о которой отзыв;
     * @param author           пользователь, добавляющий отзыв.
     * @return Объект для вывода данных.
     */
    public Comment toCommentOnCreate(CommentCreateDto commentCreateDto, Item item, User author) {
        return Comment.builder()
                .text(commentCreateDto.getText())
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build();
    }

    /**
     * Метод преобразования выходных данных по отзывам.
     *
     * @param comments список отзывов для вывода.
     * @return Список объектов для вывода данных.
     */
    public List<CommentDto> toCommentDtoList(List<Comment> comments) {
        return comments.stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());
    }
}
