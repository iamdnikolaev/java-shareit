package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserMapper {
    public UserDto toUserDto(User user);

    public User toUserOnCreate (UserCreateDto userCreateDto);

    public void toUserOnUpdate (User user, UserUpdateDto userUpdateDto);
}
