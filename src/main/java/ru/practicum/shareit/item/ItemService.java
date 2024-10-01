package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    ItemDto add(ItemCreateDto newItem);

    ItemDto update(ItemUpdateDto item);

    ItemDto getById(long itemId);

    List<ItemDto> findAllByOwnerId(long userId);

    List<ItemDto> search(String text, long userId);
}
