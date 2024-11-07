package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingDatesComments;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    ItemDto add(ItemCreateDto newItem);

    ItemDto update(ItemUpdateDto item);

    ItemDtoBookingDatesComments getById(long itemId);

    List<ItemDtoBookingDatesComments> findAllByOwnerId(long userId);

    List<ItemDto> search(String text, long userId);

    CommentDto addComment(CommentCreateDto commentCreateDto);
}
