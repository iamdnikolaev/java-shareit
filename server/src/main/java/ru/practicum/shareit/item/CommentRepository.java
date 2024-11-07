package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select c " +
            "from Comment c " +
            "join c.item i " +
            "join c.author a " +
            "where i.id in :itemIds "
    )
    List<Comment> getCommentsByItemIds(@Param("itemIds") List<Long> itemIds);
}
