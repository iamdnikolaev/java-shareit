package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemMapper {
    public ItemDto toItemDto(Item item);

    public Item toItemOnCreate (ItemCreateDto itemCreateDto, User owner);

    public void toItemOnUpdate (Item item, ItemUpdateDto itemUpdateDto);

    public List<ItemDto> toItemDtoList(List<Item> items);
}
