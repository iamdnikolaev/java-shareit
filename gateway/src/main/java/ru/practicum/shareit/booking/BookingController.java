package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
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
    public ResponseEntity<Object> add(@RequestHeader(SHARER_USER_ID) long userId,
                                      @Valid @RequestBody BookingCreateDto bookingCreateDto) {
        log.info("==> add userId = {}, bookingCreateDto = {}", userId, bookingCreateDto);
        bookingCreateDto.setUserId(userId);
        ResponseEntity<Object> bookingDto = bookingClient.add(bookingCreateDto);
        log.info("<== {}", bookingDto);

        return bookingDto;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(SHARER_USER_ID) long userId,
                                          @PathVariable long bookingId,
                                          @RequestParam Boolean approved) {
        log.info("==> approve userId = {}, bookingId = {}, approved = {}", userId, bookingId, approved);
        ResponseEntity<Object> bookingDto = bookingClient.approve(userId, bookingId, approved);
        log.info("<== {}", bookingDto);

        return bookingDto;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(@RequestHeader(SHARER_USER_ID) long userId,
                                      @PathVariable long bookingId) {
        log.info("==> get userId = {}, bookingId = {}", userId, bookingId);
        ResponseEntity<Object> bookingDto = bookingClient.getById(userId, bookingId);
        log.info("<== {}", bookingDto);

        return bookingDto;
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByBooker(@RequestHeader(SHARER_USER_ID) long userId,
                                                      @RequestParam(name = "state", defaultValue = "ALL")
                                                      BookingState state) {
        log.info("==> getBookingsByBooker userId = {}, state = {}", userId, state);
        ResponseEntity<Object> bookingsDto = bookingClient.getBookingsByBooker(userId, state);
        log.info("<== {}", bookingsDto);

        return bookingsDto;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader(SHARER_USER_ID) long userId,
                                                     @RequestParam(name = "state", defaultValue = "ALL")
                                                     BookingState state) {
        log.info("==> getBookingsByOwner userId = {}, state = {}", userId, state);
        ResponseEntity<Object> bookingsDto = bookingClient.getBookingsByOwner(userId, state);
        log.info("<== {}", bookingsDto);

        return bookingsDto;
    }
}
