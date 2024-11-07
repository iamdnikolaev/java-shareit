package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapperImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Преобразователь данных ввода-вывода по бронированиям.
 */

@Component
@RequiredArgsConstructor
public class BookingMapperImpl implements BookingMapper {

    private final UserMapperImpl userMapper;
    private final ItemMapperImpl itemMapper;

    /**
     * Метод преобразования выходных данных по бронированию.
     *
     * @param booking бронь для вывода.
     * @return Объект для вывода данных.
     */
    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(itemMapper.toItemDto(booking.getItem()))
                .booker(userMapper.toUserDto(booking.getBooker()))
                .build();
    }

    /**
     * Метод использования входных данных при создании брони.
     *
     * @param bookingCreateDto объект с данными для создания;
     * @param booker           пользователь, выполняющий бронирование;
     * @param item             вещь для бронирования;
     * @return Объект для вывода данных.
     */
    public Booking toBookingOnCreate(BookingCreateDto bookingCreateDto, Item item, User booker) {
        return Booking.builder()
                .start(bookingCreateDto.getStart())
                .end(bookingCreateDto.getEnd())
                .item(item)
                .booker(booker)
                .build();
    }

    /**
     * Метод преобразования выходных данных по бронированиям.
     *
     * @param bookings список бронирований для вывода.
     * @return Список объектов для вывода данных.
     */
    public List<BookingDto> toBookingDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toBookingDto)
                .collect(Collectors.toList());
    }
}
