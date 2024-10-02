package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserService {
    UserDto add(UserCreateDto newUser);

    UserDto update(UserUpdateDto user);

    UserDto getById(long userId);

    void delete(long userId);
}
