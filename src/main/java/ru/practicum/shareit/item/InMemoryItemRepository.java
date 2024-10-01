package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Хранилище данных о вещах с реализацией в ОЗУ.
 */
@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    /**
     * Внутренний счетчик идентификаторов вещей.
     */
    private long idCounter = 0L;

    /**
     * Собственно хранилище вещей.
     */
    private final Map<Long, Item> items = new HashMap<>();

    /**
     * Вещи по владельцам. Объект для ускорения поиска по владельцу.
     */
    private final Map<Long, Set<Item>> itemsByOwnerId = new HashMap<>();

    /**
     * Метод для генерации уникальных идентификаторов.
     * @return Новый id вещи.
     */
    private long getNextId() {
        return ++idCounter;
    }

    /**
     * Метод добавления.
     * @param newItem вещь для добавления со всеми необходимыми атрибутами.
     * @return Объект добавленной вещи.
     */
    @Override
    public Item add(Item newItem) {
        newItem.setId(getNextId());
        update(newItem); // Тут метод делает то же самое

        return newItem;
    }

    /**
     * Метод изменения.
     * @param item вещь для изменения с необходимыми атрибутами.
     * @return Объект измененной вещи.
     */
    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        long ownerId = item.getOwner().getId();
        Set<Item> setOfItems = itemsByOwnerId.get(ownerId);
        if (setOfItems == null) {
            setOfItems = new HashSet<>();
            itemsByOwnerId.put(ownerId, setOfItems);
        }
        setOfItems.add(item);

        return item;
    }

    /**
     * Метод получения данных о вещи по идентификатору.
     * @param itemId идентификатор вещи.
     * @return Опциональный объект найденной вещи.
     */
    @Override
    public Optional<Item> getById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    /**
     * Метод поиска вещей владельца.
     * @param ownerId идентификатор владельца.
     * @return Список вещей.
     */

    @Override
    public List<Item> findAllByOwnerId(long ownerId) {
        if (itemsByOwnerId.get(ownerId) == null) {
            return Collections.emptyList();
        }
        return itemsByOwnerId.get(ownerId).stream().toList();
    }

    /**
     * Метод поиска по наименованию и/или описанию вещи среди доступных для аренды.
     * @param text строка поиска.
     * @return Список вещей.
     */
    public List<Item> search(String text) {
        String textUp = text.toUpperCase();

        return items.values().stream()
                .filter(item -> item.getAvailable() && (item.getName().toUpperCase().contains(textUp)
                        || item.getDescription() != null && item.getDescription().toUpperCase().contains(textUp)))
                .collect(Collectors.toList());
    }
}
