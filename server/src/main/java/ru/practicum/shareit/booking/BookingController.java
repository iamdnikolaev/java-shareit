package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

import static ru.practicum.shareit.util.Const.SHARER_USER_ID;

/**
 * Контроллер обработки запросов по бронированию вещи.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    /**
     * Сервис работы с бронированиями
     */
    private final BookingService bookingService;

    /**
     * Метод бронирования вещи
     *
     * @param userId           идентификатор бронирующего пользователя;
     * @param bookingCreateDto атрибуты брони;
     * @return Данные по созданной брони.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto add(@RequestHeader(SHARER_USER_ID) long userId,
                          @RequestBody BookingCreateDto bookingCreateDto) {
        log.info("==> add userId = {}, bookingCreateDto = {}", userId, bookingCreateDto);
        bookingCreateDto.setUserId(userId);
        BookingDto bookingDto = bookingService.add(bookingCreateDto);
        log.info("<== {}", bookingDto);

        return bookingDto;
    }

    /**
     * Метод подтверждения или отклонения брони.
     *
     * @param userId    идентификатор владельца;
     * @param bookingId идентификатор бронирования для обработки;
     * @param approved  подтверждение (true) или отклонение (false) брони;
     * @return Данные по брони.
     */
    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(SHARER_USER_ID) long userId,
                              @PathVariable long bookingId,
                              @RequestParam Boolean approved) {
        log.info("==> approve userId = {}, bookingId = {}, approved = {}", userId, bookingId, approved);
        BookingDto bookingDto = bookingService.approve(userId, bookingId, approved);
        log.info("<== {}", bookingDto);

        return bookingDto;
    }

    /**
     * Метод получения данных по бронированию.
     *
     * @param userId    идентификатор пользователя для получения данных;
     * @param bookingId идентификатор брони для запроса;
     * @return Данные по найденной брони.
     */
    @GetMapping("/{bookingId}")
    public BookingDto get(@RequestHeader(SHARER_USER_ID) long userId,
                          @PathVariable long bookingId) {
        log.info("==> get userId = {}, bookingId = {}", userId, bookingId);
        BookingDto bookingDto = bookingService.getById(userId, bookingId);
        log.info("<== {}", bookingDto);

        return bookingDto;
    }

    /**
     * Получение списка всех бронирований для текущего пользователя.
     *
     * @param userId идентификатор пользователя для получения данных;
     * @param state  состояния бронирований для отбора;
     * @return Данные по найденным бронированиям.
     */
    @GetMapping
    public List<BookingDto> getBookingsByBooker(@RequestHeader(SHARER_USER_ID) long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        log.info("==> getBookingsByBooker userId = {}, state = {}", userId, state);
        List<BookingDto> bookingsDto = bookingService.getBookingsByBooker(userId, state);
        log.info("<== {}", bookingsDto);

        return bookingsDto;
    }

    /**
     * Получение списка всех бронирований по вещам текущего пользователя-владельца.
     *
     * @param userId идентификатор пользователя для получения данных;
     * @param state  состояния бронирований для отбора;
     * @return Данные по найденным бронированиям.
     */
    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(@RequestHeader(SHARER_USER_ID) long userId,
                                               @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        log.info("==> getBookingsByOwner userId = {}, state = {}", userId, state);
        List<BookingDto> bookingsDto = bookingService.getBookingsByOwner(userId, state);
        log.info("<== {}", bookingsDto);

        return bookingsDto;
    }
}