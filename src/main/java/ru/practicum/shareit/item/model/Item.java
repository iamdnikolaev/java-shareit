package ru.practicum.shareit.item.model;

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
    private String name;

    /**
     * Развернутое описание.
     */
    private String description;

    /**
     * Статус доступности вещи для аренды.
     */
    private Boolean available;

    /**
     * Владелец вещи {@link ru.practicum.shareit.user.User}.
     */
    private User owner;

    /**
     * Запрос на создание вещи {@link ItemRequest}.
     */
    private ItemRequest request;
}
