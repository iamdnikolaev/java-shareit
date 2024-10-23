package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface BookingMapper {
    BookingDto toBookingDto(Booking booking);

    Booking toBookingOnCreate(BookingCreateDto bookingCreateDto, Item item, User booker);

    List<BookingDto> toBookingDtoList(List<Booking> bookings);
}
