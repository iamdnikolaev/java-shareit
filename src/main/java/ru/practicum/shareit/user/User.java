package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Пользователь сервиса.
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "email")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    private Long id;

    /**
     * Имя или логин пользователя.
     */
    @Size(max = 32, message = "Максимальная длина имени - 32 символа.")
    @NotBlank(message = "Имя/логин не может быть пустым")
    private String name;

    /**
     * Адрес электронной почты.
     */
    @NotBlank(message = "Электронная почта не может быть пустой и должна содержать символ @.")
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @.")
    private String email;
}
