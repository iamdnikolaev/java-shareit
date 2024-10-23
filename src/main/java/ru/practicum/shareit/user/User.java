package ru.practicum.shareit.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "users")
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * Имя или логин пользователя.
     */
    @Column(name = "name")
    private String name;

    /**
     * Адрес электронной почты.
     */
    @Column(name = "email")
    private String email;
}
