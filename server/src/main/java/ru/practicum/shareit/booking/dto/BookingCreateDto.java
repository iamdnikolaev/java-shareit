package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Данные для создания брони {@link ru.practicum.shareit.booking.Booking}
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingCreateDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long userId;
}
