package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Данные для изменения пользователя
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserUpdateDto {
    private Long id;

    @Size(min = 1, max = 255, message = "Максимальная длина имени - 255 символов.")
    private String name;

    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @.")
    @Size(min = 5, max = 100, message = "Максимальная длина адреса электронной почты - 100 символов, минимальная - 5.")
    private String email;
}