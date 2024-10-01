package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * Запрос вещи.
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequest {
    /**
     * Уникальный идентификатор запроса.
     */
    private Long id;

    /**
     * Текст запроса, содержащий описание требуемой вещи.
     */
    @Size(max = 255, message = "Максимальная длина текста запроса - 255 символов.")
    @NotBlank(message = "Запрос не может быть пустым.")
    private String description;

    /**
     * Пользователь, создавший запрос {@link ru.practicum.shareit.user.User}.
     */
    @NotNull(message = "Необходимо указать создателя запроса.")
    private User requestor;

    /**
     * Дата и временя создания запроса.
     */
    private LocalDateTime created;
}
