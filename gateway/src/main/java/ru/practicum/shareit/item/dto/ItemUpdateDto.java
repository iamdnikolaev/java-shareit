package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Данные для изменения вещи
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemUpdateDto {
    private Long id;

    @Size(max = 100, message = "Максимальная длина краткого названия - 100 символов.")
    private String name;

    @Size(max = 255, message = "Максимальная длина описания - 255 символов.")
    private String description;

    private Boolean available;
    private Long userId;
}
