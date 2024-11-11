package ru.practicum.shareit.item.dto;

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
    private String name;
    private String description;

    @BooleanFlag
    private Boolean available;
    private Long requestId;
    private Long userId;
}
