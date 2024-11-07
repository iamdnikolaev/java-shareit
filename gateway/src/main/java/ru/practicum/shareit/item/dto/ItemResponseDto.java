package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Данные, возвращаемые по вещи в ответ на запрос
 */
@Data
@AllArgsConstructor
@Builder
public class ItemResponseDto {
    private Long itemId;
    private String name;
    private Long ownerId;
}