package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Данные для создания пользователя {@link ru.practicum.shareit.user.User}
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "email")
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @Size(min = 1, max = 32, message = "Максимальная длина имени - 32 символа.")
    @NotBlank(message = "Имя/логин не может быть пустым")
    private String name;

    @NotBlank(message = "Электронная почта не может быть пустой и должна содержать символ @.")
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @.")
    @Size(min = 5, max = 32, message = "Максимальная длина адреса электронной почты - 32 символа, минимальная - 5.")
    private String email;
}
