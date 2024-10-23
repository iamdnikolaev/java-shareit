package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Данные, возвращаемые по пользователю {@link ru.practicum.shareit.user.User}
 */
@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
