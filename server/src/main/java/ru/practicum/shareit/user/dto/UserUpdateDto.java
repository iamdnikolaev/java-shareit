package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Данные для изменения пользователя {@link ru.practicum.shareit.user.User}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class UserUpdateDto {
    private Long id;
    private String name;
    private String email;
}