package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRequestService itemRequestService;

    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void addNewUser() {

        UserDto userDto = userService.add(new UserCreateDto("Основной О. О.", "main@yandex.ru"));

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isNotNull();
        assertThat(userDto.getName()).isEqualTo("Основной О. О.");
        assertThat(userDto.getEmail()).isEqualTo("main@yandex.ru");
    }

    @Test
    void updateUser() {
        UserDto userDto = userService.add(new UserCreateDto("Основной О. О.", "main@yandex.ru"));

        UserDto userDtoUpdated = userService.update(new UserUpdateDto(userDto.getId(), "Совершенный О. О.",
                "perfect@yandex.ru"));

        assertThat(userDtoUpdated).isNotNull();
        assertThat(userDtoUpdated.getId()).isEqualTo(userDto.getId());
        assertThat(userDtoUpdated.getName()).isEqualTo("Совершенный О. О.");
        assertThat(userDtoUpdated.getEmail()).isEqualTo("perfect@yandex.ru");
    }

    @Test
    void getUserById() {
        UserDto userDto = userService.add(new UserCreateDto("Основной О. О.", "main@yandex.ru"));

        UserDto userDtoFrom = userService.getById(userDto.getId());

        assertThat(userDtoFrom).isNotNull();
        assertThat(userDtoFrom.getId()).isEqualTo(userDto.getId());
        assertThat(userDtoFrom.getName()).isEqualTo(userDto.getName());
        assertThat(userDtoFrom.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void deleteUser() {
        UserDto userDto = userService.add(new UserCreateDto("Основной О. О.", "main@yandex.ru"));

        userService.delete(userDto.getId());

        assertThrows(NotFoundException.class, () -> {
            userService.getById(userDto.getId());
        }, "Пользователь не должен быть найден после удаления");

    }
}