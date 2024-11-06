package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRequestService itemRequestService;

    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private UserDto userDto;
    private UserDto bookerDto;
    private UserDto anyUserDto;
    private ItemDto itemDto;
    private ItemDto itemDto2;
    private ItemDto itemDto3;

    public static final long RESULT_ID_TEST = 1L;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();

        userDto = userService.add(new UserCreateDto("commonUser", "commonUser@mail.org"));
        bookerDto = userService.add(new UserCreateDto("commonBooker", "commonBooker@mail.org"));
        anyUserDto = userService.add(new UserCreateDto("anyUser", "anyUser@mail.org"));
        itemDto = itemService.add(new ItemCreateDto("Дрель", "Дрель с ударным механизмом (1000 Вт)",
                true, null, userDto.getId()));
        itemDto2 = itemService.add(new ItemCreateDto("Кефир", "Кефир слабоалкогольный (1,5 %) " +
                "для распития на рабочем месте",
                true, null, userDto.getId()));
        itemDto3 = itemService.add(new ItemCreateDto("Бубен", "Бубен шамана Кулана. Бить чаще в " +
                "дни дедлайнов", true, null, userDto.getId()));
    }

    @Test
    void addNewBooking() {
        BookingDto bookingDto = bookingService.add(new BookingCreateDto(LocalDateTime.now()
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS),
                itemDto.getId(), bookerDto.getId()));

        assertThat(bookingDto).isNotNull();
        assertThat(bookingDto.getId()).isNotNull();
        assertThat(bookingDto.getStatus()).isEqualTo(BookingStatus.WAITING);
        assertThat(bookingDto.getItem().getId()).isEqualTo(itemDto.getId());
        assertThat(bookingDto.getBooker().getId()).isEqualTo(bookerDto.getId());
    }

    @Test
    void addNewBookingUnavailable() {
        itemDto = itemService.update(new ItemUpdateDto(itemDto.getId(), "КефираНет", "Кефир " +
                "слабоалкогольный (1,5 %) закончился", false, userDto.getId()));

        assertThrows(ValidationException.class, () -> {
            bookingService.add(new BookingCreateDto(LocalDateTime.now()
                    .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS),
                    itemDto.getId(), bookerDto.getId()));
        }, "Недоступную вещь нельзя забронировать");
    }

    @Test
    void addNewBookingWrongDates() {
        assertThrows(ValidationException.class, () -> {
            bookingService.add(new BookingCreateDto(LocalDateTime.now()
                    .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().truncatedTo(ChronoUnit.HOURS),
                    itemDto.getId(), bookerDto.getId()));
        }, "Совпадение дат начала и окончания бронирования должно привести к ошибке");

        assertThrows(ValidationException.class, () -> {
            bookingService.add(new BookingCreateDto(LocalDateTime.now().plusHours(1), LocalDateTime.now()
                    .plusMinutes(30), itemDto.getId(), bookerDto.getId()));
        }, "Дата начала бронирования не может быть позже даты окончания - это ошибка");

        BookingDto bookingDto = bookingService.add(new BookingCreateDto(LocalDateTime.now()
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS),
                itemDto.getId(), bookerDto.getId()));

        assertThrows(ConflictException.class, () -> {
            bookingService.add(new BookingCreateDto(LocalDateTime.now(), LocalDateTime.now()
                    .plusMinutes(40), itemDto.getId(), bookerDto.getId()));
        }, "Пересечение броней по времени должно приводить к ошибке");
    }

    @Test
    void approveBooking() {
        BookingDto bookingDto = bookingService.add(new BookingCreateDto(LocalDateTime.now()
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS),
                itemDto.getId(), bookerDto.getId()));

        BookingDto bookingDtoApproved = bookingService.approve(userDto.getId(), bookingDto.getId(), true);

        assertThat(bookingDtoApproved).isNotNull();
        assertThat(bookingDtoApproved.getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(bookingDtoApproved.getItem().getId()).isEqualTo(bookingDto.getItem().getId());
        assertThat(bookingDtoApproved.getBooker().getId()).isEqualTo(bookerDto.getId());
    }

    @Test
    void approveBookingByNonOwner() {
        BookingDto bookingDto = bookingService.add(new BookingCreateDto(LocalDateTime.now()
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS),
                itemDto.getId(), bookerDto.getId()));

        assertThrows(ValidationException.class, () -> {
            bookingService.approve(bookerDto.getId(), bookingDto.getId(), true);
        }, "Невладелец не должен уметь подтверждать бронирование");
    }

    @Test
    void getBookingById() {
        BookingDto bookingDto = bookingService.add(new BookingCreateDto(LocalDateTime.now()
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS),
                itemDto.getId(), bookerDto.getId()));

        BookingDto bookingDtoFrom = bookingService.getById(bookerDto.getId(), bookingDto.getId());

        assertThat(bookingDtoFrom).isNotNull();
        assertThat(bookingDtoFrom.getId()).isEqualTo(bookingDto.getId());
        assertThat(bookingDtoFrom.getStart()).isEqualTo(bookingDto.getStart());
        assertThat(bookingDtoFrom.getEnd()).isEqualTo(bookingDto.getEnd());
        assertThat(bookingDtoFrom.getStatus()).isEqualTo(bookingDto.getStatus());
        assertThat(bookingDtoFrom.getItem().getId()).isEqualTo(itemDto.getId());
        assertThat(bookingDtoFrom.getBooker().getId()).isEqualTo(bookerDto.getId());
    }

    @Test
    void getBookingByIdByWrongUser() {
        BookingDto bookingDto = bookingService.add(new BookingCreateDto(LocalDateTime.now()
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS),
                itemDto.getId(), bookerDto.getId()));

        assertThrows(ValidationException.class, () -> {
            bookingService.getById(anyUserDto.getId(), bookingDto.getId());
        }, "Посторонний не должен получать данные бронирования");
    }

    @Test
    void getBookingByIdByZeroUser() {
        BookingDto bookingDto = bookingService.add(new BookingCreateDto(LocalDateTime.now()
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS),
                itemDto.getId(), bookerDto.getId()));

        assertThrows(ValidationException.class, () -> {
            bookingService.getById(0L, bookingDto.getId());
        }, "Неверный вызов с нулевым ИД пользователя должен привести к ошибке");
    }

    @Test
    void getBookingByIdZero() {
        BookingDto bookingDto = bookingService.add(new BookingCreateDto(LocalDateTime.now()
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS),
                itemDto.getId(), bookerDto.getId()));

        assertThrows(ValidationException.class, () -> {
            bookingService.getById(bookerDto.getId(), 0L);
        }, "Неверный вызов с нулевым ИД брони должен привести к ошибке");
    }

    @Test
    void getBookingsByBooker() {
        BookingDto bookingDto = bookingService.add(new BookingCreateDto(LocalDateTime.now()
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS),
                itemDto.getId(), bookerDto.getId()));

        BookingDto bookingDto2 = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusHours(1)
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(9).truncatedTo(ChronoUnit.HOURS),
                itemDto2.getId(), bookerDto.getId()));

        BookingDto bookingDto3 = bookingService.add(new BookingCreateDto(LocalDateTime.now(), LocalDateTime.now()
                .plusNanos(1), itemDto3.getId(), bookerDto.getId()));

        List<BookingDto> bookingDtos = bookingService.getBookingsByBooker(bookerDto.getId(), BookingState.ALL);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(3);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto2.getItem().getId());
        assertThat(bookingDtos.getLast().getItem().getId()).isEqualTo(bookingDto.getItem().getId());

        bookingDtos = bookingService.getBookingsByBooker(bookerDto.getId(), BookingState.CURRENT);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(1);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto.getItem().getId());

        bookingDtos = bookingService.getBookingsByBooker(bookerDto.getId(), BookingState.FUTURE);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(1);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto2.getItem().getId());

        bookingDtos = bookingService.getBookingsByBooker(bookerDto.getId(), BookingState.WAITING);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(3);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto2.getItem().getId());
        assertThat(bookingDtos.getLast().getItem().getId()).isEqualTo(bookingDto.getItem().getId());

        bookingService.approve(userDto.getId(), bookingDto.getId(), false);
        bookingDtos = bookingService.getBookingsByBooker(bookerDto.getId(), BookingState.REJECTED);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(1);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto.getItem().getId());

        bookingDtos = bookingService.getBookingsByBooker(bookerDto.getId(), BookingState.PAST);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(1);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto3.getItem().getId());

        assertThrows(ValidationException.class, () -> {
            bookingService.getBookingsByBooker(bookerDto.getId(), null);
        }, "По неверно заданному статусу нельзя найти бронирования");
    }

    @Test
    void getBookingsByOwner() {
        BookingDto bookingDto = bookingService.add(new BookingCreateDto(LocalDateTime.now()
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS),
                itemDto.getId(), bookerDto.getId()));

        BookingDto bookingDto2 = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusHours(1)
                .truncatedTo(ChronoUnit.HOURS), LocalDateTime.now().plusHours(9).truncatedTo(ChronoUnit.HOURS),
                itemDto2.getId(), bookerDto.getId()));

        BookingDto bookingDto3 = bookingService.add(new BookingCreateDto(LocalDateTime.now(), LocalDateTime.now()
                .plusNanos(1), itemDto3.getId(), bookerDto.getId()));

        List<BookingDto> bookingDtos = bookingService.getBookingsByOwner(userDto.getId(), BookingState.ALL);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(3);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto2.getItem().getId());
        assertThat(bookingDtos.getLast().getItem().getId()).isEqualTo(bookingDto.getItem().getId());

        bookingDtos = bookingService.getBookingsByOwner(userDto.getId(), BookingState.CURRENT);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(1);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto.getItem().getId());

        bookingDtos = bookingService.getBookingsByOwner(userDto.getId(), BookingState.FUTURE);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(1);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto2.getItem().getId());

        bookingDtos = bookingService.getBookingsByOwner(userDto.getId(), BookingState.WAITING);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(3);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto2.getItem().getId());
        assertThat(bookingDtos.getLast().getItem().getId()).isEqualTo(bookingDto.getItem().getId());

        bookingService.approve(userDto.getId(), bookingDto.getId(), false);
        bookingDtos = bookingService.getBookingsByOwner(userDto.getId(), BookingState.REJECTED);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(1);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto.getItem().getId());

        bookingDtos = bookingService.getBookingsByOwner(userDto.getId(), BookingState.PAST);
        assertThat(bookingDtos).isNotNull();
        assertThat(bookingDtos).hasSize(1);
        assertThat(bookingDtos.getFirst().getItem().getId()).isEqualTo(bookingDto3.getItem().getId());

        assertThrows(ValidationException.class, () -> {
            bookingService.getBookingsByOwner(userDto.getId(), null);
        }, "По неверно заданному статусу нельзя найти бронирования");
    }
}