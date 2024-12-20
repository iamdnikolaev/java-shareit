package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Данные, возвращаемые по отзыву о вещи {@link ru.practicum.shareit.item.model.Item}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private Long itemId;
    private String authorName;
    private LocalDateTime created;
}
