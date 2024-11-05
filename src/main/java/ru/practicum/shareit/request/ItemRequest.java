package ru.practicum.shareit.request;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "requests")
public class ItemRequest {
    /**
     * Уникальный идентификатор запроса.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    /**
     * Текст запроса, содержащий описание требуемой вещи.
     */
    @Size(max = 255, message = "Максимальная длина текста запроса - 255 символов.")
    @NotBlank(message = "Запрос не может быть пустым.")
    @Column(name = "description")
    private String description;

    /**
     * Пользователь, создавший запрос {@link ru.practicum.shareit.user.User}.
     */
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    @NotNull(message = "Необходимо указать создателя запроса.")
    private User requestor;

    /**
     * Дата и время создания запроса.
     */
    @Column(name = "created")
    private LocalDateTime created;
}
