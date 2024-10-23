package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Данные, возвращаемые по вещи {@link ru.practicum.shareit.item.model.Item} с указанием дат последнего и ближайшего
 * бронирования и отзывов
 */
@Data
@AllArgsConstructor
@Builder
public class ItemDtoBookingDatesComments {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<CommentDto> comments;
}
