package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

/**
 * Контроллер работы с пользователями.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    /**
     * Сервис по работе с пользователями.
     */
    private final UserService userService;

    /**
     * Метод добавления пользователя.
     *
     * @param userCreateDto атрибуты пользователя для добавления.
     * @return Данные по добавленному пользователю.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto add(@RequestBody UserCreateDto userCreateDto) {
        log.info("==> add by userCreateDto = {}", userCreateDto);
        UserDto userDto = userService.add(userCreateDto);
        log.info("<== {}", userDto);

        return userDto;
    }

    /**
     * Метод изменения пользователя.
     *
     * @param userId        идентификатор пользователя для изменения.
     * @param userUpdateDto атрибуты пользователя для изменения.
     * @return Данные по измененному пользователю.
     */
    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId,
                          @RequestBody UserUpdateDto userUpdateDto) {
        log.info("==> update by userId = {}, userUpdateDto = {}", userId, userUpdateDto);
        userUpdateDto.setId(userId);
        UserDto userDto = userService.update(userUpdateDto);
        log.info("<== {}", userDto);

        return userDto;
    }

    /**
     * Метод получения данных о пользователе.
     *
     * @param userId идентификатор пользователя;
     * @return Данные по найденному пользователю.
     */
    @GetMapping("/{userId}")
    public UserDto get(@PathVariable long userId) {
        log.info("==> get by userId = {}", userId);
        UserDto userDto = userService.getById(userId);
        log.info("<== {}", userDto);

        return userDto;
    }

    /**
     * Метод удаления пользователя.
     *
     * @param userId идентификатор пользователя;
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("==> delete for userId = {}", userId);
        userService.delete(userId);
        log.info("<== User deleted");
    }
}