package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item add(Item newItem);

    Item update(Item item);

    Optional<Item> getById(long itemId);

    List<Item> findAllByOwnerId(long ownerId);

    List<Item> search(String text);
}
