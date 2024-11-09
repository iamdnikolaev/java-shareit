package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceTest {

    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();

        userDto = userService.add(new UserCreateDto("commonUser", "commonUser@mail.org"));
    }

    @Test
    void addNewRequest() {
        ItemRequestDto requestDto = itemRequestService.add(new ItemRequestCreateDto("Need some big instrument",
                userDto.getId()));

        assertThat(requestDto).isNotNull();
        assertThat(requestDto.getId()).isNotNull();
        assertThat(requestDto.getDescription()).isEqualTo("Need some big instrument");
        assertThat(requestDto.getCreated().isBefore(LocalDateTime.now()));
        assertThat(requestDto.getItems()).isEmpty();
    }

    @Test
    void getRequestsByRequestorId() {
        ItemRequestDto requestDto = itemRequestService.add(new ItemRequestCreateDto("Нужен большой инструмент",
                userDto.getId()));

        ItemRequestDto requestDto2 = itemRequestService.add(new ItemRequestCreateDto("Нужно большое устройство",
                userDto.getId()));

        ItemRequestDto requestDto3 = itemRequestService.add(new ItemRequestCreateDto("Нужен тяжелый молот",
                userDto.getId()));

        ItemDto itemDto = itemService.add(new ItemCreateDto("Перфоратор", "Перфоратор профессиональный " +
                "мощностью 2000 Вт", true, requestDto.getId(), userDto.getId()));

        List<ItemRequestDto> requestDtos = itemRequestService.getRequestsByRequestorId(userDto.getId());

        assertThat(requestDtos).isNotNull();
        assertThat(requestDtos).hasSize(3);
    }

    @Test
    void getRequestsByRequestorIdWrong() {
        assertThrows(NotFoundException.class, () -> {
            itemRequestService.getRequestsByRequestorId(1234L);
        }, "Requestor cannot be found by id = 1234");
    }

    @Test
    void getRequestsByOtherUsers() {
        UserDto otherUser = userService.add(new UserCreateDto("Other User", "otheruser@google.com"));
        UserDto otherUser2 = userService.add(new UserCreateDto("Other User2", "otheruser2@google.com"));

        ItemRequestDto requestDto = itemRequestService.add(new ItemRequestCreateDto("Need some big instrument",
                userDto.getId()));

        ItemRequestDto requestDto2 = itemRequestService.add(new ItemRequestCreateDto("Need some big device",
                otherUser.getId()));
        ItemCreateDto itemTV = new ItemCreateDto("Телевизор Samsung 70 дюймов",
                "Samsung UE70TU7100U 2024 года выпуска", true, requestDto2.getId(), userDto.getId());
        itemService.add(itemTV);
        ItemCreateDto itemTV2 = new ItemCreateDto("Телевизор LG 70 дюймов", "LG 70UN73506LB 2023 года выпуска",
                true, requestDto2.getId(), userDto.getId());
        itemService.add(itemTV2);

        ItemRequestDto requestDto3 = itemRequestService.add(new ItemRequestCreateDto("Need some big hammer",
                otherUser2.getId()));
        ItemCreateDto itemHummer = new ItemCreateDto("Молот ковочный",
                "Молот ковочный МА4129А пневматический кузнечный", true, requestDto3.getId(),
                userDto.getId());
        itemService.add(itemHummer);

        List<ItemRequestDto> requestDtos = itemRequestService.getRequestsByOtherUsers(userDto.getId());

        assertThat(requestDtos).isNotNull();
        assertThat(requestDtos).hasSize(2);
        assertTrue(requestDtos.contains(requestDto2), "Запрос Need some big device должен быть найден.");
        assertTrue(requestDtos.contains(requestDto3), "Запрос Need some big hammer должен быть найден.");
        assertThat(requestDtos.getFirst().getItems()).hasSize(1);
        assertThat(requestDtos.getLast().getItems()).hasSize(2);
    }

    @Test
    void getById() {
        ItemRequestDto requestDto = itemRequestService.add(new ItemRequestCreateDto("Need some big instrument",
                userDto.getId()));

        ItemRequestDto requestDto2 = itemRequestService.add(new ItemRequestCreateDto("Need some big device",
                userDto.getId()));

        ItemRequestDto requestDto3 = itemRequestService.add(new ItemRequestCreateDto("Need some big hammer",
                userDto.getId()));

        ItemRequestDto requestDtoResult = itemRequestService.getById(userDto.getId(), requestDto.getId());

        assertThat(requestDtoResult).isNotNull();
        assertThat(requestDtoResult.getDescription()).isEqualTo("Need some big instrument");
        assertThat(requestDtoResult.getCreated().isBefore(LocalDateTime.now()));

        ItemRequestDto requestDtoResult2 = itemRequestService.getById(userDto.getId(), requestDto2.getId());

        assertThat(requestDtoResult2).isNotNull();
        assertThat(requestDtoResult2.getDescription()).isEqualTo("Need some big device");
        assertThat(requestDtoResult2.getCreated().isBefore(LocalDateTime.now()));
    }

    @Test
    void getByIdWrong() {
        assertThrows(NotFoundException.class, () -> {
            itemRequestService.getById(userDto.getId(), 5555L);
        }, "Request cannot be found by id = 5555");
    }
}