package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Необходимо указать дату начала бронирования.")
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull(message = "Необходимо указать дату конца бронирования.")
    @Future
    private LocalDateTime end;

    @NotNull(message = "Необходимо указать вещь для бронирования.")
    private Long itemId;

    private Long userId;
}
