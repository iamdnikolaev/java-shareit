package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto add(BookingCreateDto bookingCreateDto);

    BookingDto approve(long userId, long bookingId, Boolean approved);

    BookingDto getById(long userId, long bookingId);

    List<BookingDto> getBookingsByBooker(long bookerId, BookingState state);

    List<BookingDto> getBookingsByOwner(long ownerId, BookingState state);
}
