package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * Вещь, которой можно поделиться.
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    /**
     * Уникальный идентификатор вещи.
     */
    private Long id;

    /**
     * Краткое название.
     */
    @Size(max = 100, message = "Максимальная длина краткого названия - 100 символов.")
    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    /**
     * Развернутое описание.
     */
    @Size(max = 255, message = "Максимальная длина описания - 255 символов.")
    @NotBlank(message = "Описание не может быть пустым.")
    private String description;

    /**
     * Статус доступности вещи для аренды.
     */
    @NotNull(message = "Необходимо указать (не)доступность явно.")
    private Boolean available;

    /**
     * Владелец вещи {@link ru.practicum.shareit.user.User}.
     */
    @NotNull(message = "Необходимо указать владельца вещи.")
    private User owner;

    /**
     * Запрос на создание вещи {@link ItemRequest}.
     */
    private ItemRequest request;
}
