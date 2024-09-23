package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

/**
 * Бронирование вещи.
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    /**
     * Уникальный идентификатор бронирования.
     */
    private Long id;

    /**
     * Дата и время начала бронирования.
     */
    private LocalDateTime start;

    /**
     * Дата и время конца бронирования.
     */
    private LocalDateTime end;

    /**
     * Id вещи, которую бронирует пользователь {@link Item#getId()}.
     */
    @NotNull(message = "Необходимо указать вещь для бронирования.")
    private Long item;

    /**
     * Id осуществляющего бронирование пользователя {@link ru.practicum.shareit.user.User#getId()}.
     */
    @NotNull(message = "Необходимо указать пользователя, осуществляющего бронирование.")
    private Long booker;

    /**
     * Статус бронирования {@link BookingStatus}
     */
    @NotNull(message = "Необходимо указать статус бронирования.")
    private BookingStatus status;
}
