package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(long ownerId);

    @Query("select i from Item i " +
            "where i.available = true " +
            "and (upper(i.name) like upper(concat('%',:search,'%')) " +
            "  or upper(i.description) like upper(concat('%', :search, '%')))")
    List<Item> search(@Param("search") String search);

    @Query("select i from Item i where i.request.id = :requestId")
    List<Item> getItemsByRequestId(@Param("requestId") Long requestId);
}