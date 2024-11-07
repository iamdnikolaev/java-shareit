package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.exception.ValidationException;

import static ru.practicum.shareit.util.Const.SHARER_USER_ID;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    /**
     * Клиент для связи сервером по работе с бронированиями.
     */
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> add(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                      @Valid @RequestBody BookingCreateDto bookingCreateDto) {
        log.info("==> add userId = {}, bookingCreateDto = {}", userId, bookingCreateDto);
        if (bookingCreateDto.getStart().equals(bookingCreateDto.getEnd())) {
            throw new ValidationException("Начало бронирования по дате/времени не может совпадать с его окончанием");
        }
        if (!bookingCreateDto.getEnd().isAfter(bookingCreateDto.getStart())) {
            throw new ValidationException("Окончание бронирования должно быть после даты/времени его начала");
        }

        bookingCreateDto.setUserId(userId);
        ResponseEntity<Object> bookingDto = bookingClient.add(bookingCreateDto);
        log.info("<== {}", bookingDto);

        return bookingDto;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                          @PathVariable @Positive long bookingId,
                                          @RequestParam Boolean approved) {
        log.info("==> approve userId = {}, bookingId = {}, approved = {}", userId, bookingId, approved);
        ResponseEntity<Object> bookingDto = bookingClient.approve(userId, bookingId, approved);
        log.info("<== {}", bookingDto);

        return bookingDto;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                      @PathVariable @Positive long bookingId) {
        log.info("==> get userId = {}, bookingId = {}", userId, bookingId);
        ResponseEntity<Object> bookingDto = bookingClient.getById(userId, bookingId);
        log.info("<== {}", bookingDto);

        return bookingDto;
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByBooker(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                                      @RequestParam(name = "state", defaultValue = "ALL")
                                                      BookingState state) {
        log.info("==> getBookingsByBooker userId = {}, state = {}", userId, state);
        ResponseEntity<Object> bookingsDto = bookingClient.getBookingsByBooker(userId, state);
        log.info("<== {}", bookingsDto);

        return bookingsDto;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                                     @RequestParam(name = "state", defaultValue = "ALL")
                                                     BookingState state) {
        log.info("==> getBookingsByOwner userId = {}, state = {}", userId, state);
        ResponseEntity<Object> bookingsDto = bookingClient.getBookingsByOwner(userId, state);
        log.info("<== {}", bookingsDto);

        return bookingsDto;
    }
}
