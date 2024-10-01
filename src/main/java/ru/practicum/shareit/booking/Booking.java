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
import ru.practicum.shareit.user.User;

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
    @NotNull(message = "Необходимо указать дату начала бронирования.")
    private LocalDateTime start;

    /**
     * Дата и время конца бронирования.
     */
    @NotNull(message = "Необходимо указать дату конца бронирования.")
    private LocalDateTime end;

    /**
     * Вещь, которую бронирует пользователь {@link Item}.
     */
    @NotNull(message = "Необходимо указать вещь для бронирования.")
    private Item item;

    /**
     * Осуществляющий бронирование пользователь {@link ru.practicum.shareit.user.User}.
     */
    @NotNull(message = "Необходимо указать пользователя, осуществляющего бронирование.")
    private User booker;

    /**
     * Статус бронирования {@link BookingStatus}
     */
    @NotNull(message = "Необходимо указать статус бронирования.")
    private BookingStatus status;
}
