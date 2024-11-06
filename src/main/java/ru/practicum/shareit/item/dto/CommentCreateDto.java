package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Данные для добавления отзыва о вещи {@link ru.practicum.shareit.item.model.Item}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCreateDto {
    @Size(max = 1000, message = "Максимальная длина описания - 1000 символов.")
    @NotBlank(message = "Текст комментария не может быть пустым.")
    private String text;
    private Long itemId;
    private Long authorId;
}
