package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Данные для создания вещи {@link ru.practicum.shareit.item.model.Item}
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemCreateDto {
    @Size(max = 100, message = "Максимальная длина краткого названия - 100 символов.")
    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @Size(max = 255, message = "Максимальная длина описания - 255 символов.")
    @NotBlank(message = "Описание не может быть пустым.")
    private String description;

    @BooleanFlag
    @NotNull(message = "Необходимо указать (не)доступность явно.")
    private Boolean available;

    private Long requestId;

    private Long userId;
}
