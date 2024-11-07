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
 * Отзывы о вещи после аренды.
 */
@Entity
@Table(name = "comments")
@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    /**
     * Уникальный идентификатор отзыва.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    /**
     * Текст комментария.
     */
    @Column(name = "text")
    private String text;

    /**
     * Автор отзыва {@link User}
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    /**
     * Вещь, о которой оставляется отзыв {@link Item}
     */
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    /**
     * Дата и время создания отзыва.
     */
    @Column(name = "created")
    private LocalDateTime created;
}
