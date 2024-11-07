package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис работы с бронированиями вещей.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    /**
     * Хранилище вещей.
     */
    private final ItemRepository itemRepository;

    /**
     * Хранилище пользователей.
     */
    private final UserRepository userRepository;

    /**
     * Хранилище бронирований.
     */
    private final BookingRepository bookingRepository;

    /**
     * Преобразователь данных ввода-вывода.
     */
    private final BookingMapperImpl bookingMapper;

    /**
     * Метод бронирования вещи
     *
     * @param bookingCreateDto атрибуты брони
     * @return Данные по созданной брони
     */
    @Override
    @Transactional
    public BookingDto add(BookingCreateDto bookingCreateDto) {
        long crossingCount = bookingRepository.crossingCount(bookingCreateDto.getItemId(),
                bookingCreateDto.getStart(),
                bookingCreateDto.getEnd());
        if (crossingCount > 0) {
            throw new ConflictException("Добавляемая бронь не может пересекаться с уже имеющимися бронированиями");
        }

        User booker = checkUserId(bookingCreateDto.getUserId());

        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь itemId = " + bookingCreateDto.getItemId()
                        + " не найдена"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь itemId = " + bookingCreateDto.getItemId()
                    + " недоступна для бронирования");
        }

        Booking booking = bookingMapper.toBookingOnCreate(bookingCreateDto, item, booker);
        booking.setStatus(BookingStatus.WAITING);
        long id = bookingRepository.save(booking).getId();
        booking.setId(id);

        return bookingMapper.toBookingDto(booking);
    }

    /**
     * Метод подтверждения или отклонения брони.
     *
     * @param ownerId   идентификатор владельца, выполняющего действие;
     * @param bookingId идентификатор бронирования для обработки;
     * @param approved  подтверждение (true) или отклонение (false) брони.
     * @return Данные по брони
     */
    @Override
    @Transactional
    public BookingDto approve(long ownerId, long bookingId, Boolean approved) {
        checkUserId(ownerId);
        Booking booking = checkBookingId(bookingId);

        if (!bookingRepository.getItemOwnerId(bookingId).equals(ownerId)) {
            throw new ValidationException("Пользователь не является владельцем вещи. Смена статуса бронирования запрещена.");
        }

        if (booking.getStatus().equals(BookingStatus.WAITING)) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
            bookingRepository.save(booking);
        }

        return bookingMapper.toBookingDto(booking);
    }

    /**
     * Метод получения данных по бронированию.
     *
     * @param userId    идентификатор пользователя для получения данных;
     * @param bookingId идентификатор брони для запроса;
     * @return Данные по найденной брони.
     */
    public BookingDto getById(long userId, long bookingId) {
        checkUserId(userId);
        Booking booking = checkBookingId(bookingId);

        if (!bookingRepository.getItemOwnerId(bookingId).equals(userId)
                && !booking.getBooker().getId().equals(userId)) {
            throw new ValidationException("Пользователь не является ни владельцем вещи, ни автором брони. " +
                    "Получение данных о бронировании запрещено.");
        }

        return bookingMapper.toBookingDto(booking);
    }

    /**
     * Получение списка всех бронирований для текущего пользователя.
     *
     * @param bookerId идентификатор пользователя для получения данных;
     * @param state    состояния бронирований для отбора;
     * @return Данные по найденным бронированиям.
     */
    public List<BookingDto> getBookingsByBooker(long bookerId, BookingState state) {
        checkUserId(bookerId);

        if (state != null) {
            if (state.equals(BookingState.ALL)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findAllBookingsByBookerId(bookerId));
            } else if (state.equals(BookingState.CURRENT)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findCurrentBookingsByBookerId(bookerId,
                        LocalDateTime.now()));
            } else if (state.equals(BookingState.PAST)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findPastBookingsByBookerId(bookerId,
                        LocalDateTime.now()));
            } else if (state.equals(BookingState.FUTURE)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findFutureBookingsByBookerId(bookerId,
                        LocalDateTime.now()));
            } else if (state.equals(BookingState.WAITING)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findBookingsByBookerIdAndStatus(bookerId,
                        List.of(BookingStatus.WAITING)));
            } else if (state.equals(BookingState.REJECTED)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findBookingsByBookerIdAndStatus(bookerId,
                        List.of(BookingStatus.REJECTED)));
            }
        }
        throw new ValidationException("Указано неверное состояние для отбора бронирований.");
    }

    /**
     * Получение списка всех бронирований по вещам текущего пользователя-владельца.
     *
     * @param ownerId идентификатор владельца;
     * @param state   состояния бронирований для отбора;
     * @return Данные по найденным бронированиям.
     */
    public List<BookingDto> getBookingsByOwner(long ownerId, BookingState state) {
        checkUserId(ownerId);

        if (state != null) {
            if (state.equals(BookingState.ALL)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findAllBookingsByOwnerId(ownerId));
            } else if (state.equals(BookingState.CURRENT)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findCurrentBookingsByOwnerId(ownerId,
                        LocalDateTime.now()));
            } else if (state.equals(BookingState.PAST)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findPastBookingsByOwnerId(ownerId,
                        LocalDateTime.now()));
            } else if (state.equals(BookingState.FUTURE)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findFutureBookingsByOwnerId(ownerId,
                        LocalDateTime.now()));
            } else if (state.equals(BookingState.WAITING)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findBookingsByOwnerIdAndStatus(ownerId,
                        List.of(BookingStatus.WAITING)));
            } else if (state.equals(BookingState.REJECTED)) {
                return bookingMapper.toBookingDtoList(bookingRepository.findBookingsByOwnerIdAndStatus(ownerId,
                        List.of(BookingStatus.REJECTED)));
            }
        }
        throw new ValidationException("Указано неверное состояние для отбора бронирований.");
    }

    /**
     * Метод проверки наличия указанного пользователя в хранилище.
     *
     * @param userId проверяемый идентификатор пользователя.
     * @return Найденный пользователь
     */
    private User checkUserId(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ForbiddenException("Пользователь не найден по userId = " + userId));
    }

    /**
     * Метод проверки наличия указанного бронирования в хранилище.
     *
     * @param bookingId проверяемое бронирование.
     * @return Найденное бронирование
     */
    private Booking checkBookingId(long bookingId) {
        return bookingRepository.getBookingById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено по bookingId = " + bookingId));
    }
}
