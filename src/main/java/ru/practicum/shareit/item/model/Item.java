package ru.practicum.shareit.item.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * Вещь, которой можно поделиться.
 */
@Entity
@Table(name = "items")
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    /**
     * Уникальный идентификатор вещи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    /**
     * Краткое название.
     */
    @Column(name = "name")
    private String name;

    /**
     * Развернутое описание.
     */
    @Column(name = "description")
    private String description;

    /**
     * Статус доступности вещи для аренды.
     */
    @Column(name = "is_available")
    private Boolean available;

    /**
     * Владелец вещи {@link ru.practicum.shareit.user.User}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    /**
     * Запрос на создание вещи {@link ItemRequest}.
     */
    @Transient
    private ItemRequest request;
}
