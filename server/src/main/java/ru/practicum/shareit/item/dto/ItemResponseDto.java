package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Данные, возвращаемые по вещи {@link ru.practicum.shareit.item.model.Item} в ответ на запрос
 * {@link ru.practicum.shareit.request.ItemRequest}
 */
@Data
@AllArgsConstructor
@Builder
public class ItemResponseDto {
    private Long itemId;
    private String name;
    private Long ownerId;
}