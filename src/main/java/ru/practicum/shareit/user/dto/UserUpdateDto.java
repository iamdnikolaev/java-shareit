package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
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
public class UserUpdateDto {
    private Long id;

    @Size(min = 1, max = 32, message = "Максимальная длина имени - 32 символа.")
    private String name;

    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @.")
    @Size(min = 5, max = 32, message = "Максимальная длина адреса электронной почты - 32 символа, минимальная - 5.")
    private String email;
}