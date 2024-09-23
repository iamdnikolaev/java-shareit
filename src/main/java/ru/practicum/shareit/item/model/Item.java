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
     * Краткое названия.
     */
    @Size(max = 100, message = "Максимальная краткого названия - 100 символов.")
    @NotNull(message = "Название не может быть пустым")
    @NotBlank(message = "Название не может быть пустым")
    private String name;

    /**
     * Развернутое описание.
     */
    @Size(max = 255, message = "Максимальная длина описания - 255 символов.")
    private String description;

    /**
     * Статус доступности вещи для аренды.
     */
    private Boolean available;

    /**
     * Id владельца вещи {@link ru.practicum.shareit.user.User#getId()}.
     */
    @NotNull(message = "Необходимо указать владельца вещи.")
    private Long owner;

    /**
     * Id запроса на создание вещи {@link ItemRequest#getId()}.
     */
    private Long request;
}
