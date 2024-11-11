package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Данные для создания запроса вещи
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemRequestCreateDto {
    @Size(max = 255, message = "Максимальная длина текста запроса - 255 символов.")
    @NotBlank(message = "Запрос не может быть пустым.")
    private String description;

    private Long userId;
}
