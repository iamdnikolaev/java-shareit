package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserMapper {
    UserDto toUserDto(User user);

    User toUserOnCreate(UserCreateDto userCreateDto);

    void toUserOnUpdate(User user, UserUpdateDto userUpdateDto);
}
