package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Данные для изменения вещи {@link ru.practicum.shareit.item.model.Item}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class ItemUpdateDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long userId;
}
