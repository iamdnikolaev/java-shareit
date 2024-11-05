package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingDatesComments;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ItemMapper {
    ItemDto toItemDto(Item item);

    Item toItemOnCreate(ItemCreateDto itemCreateDto, User owner);

    void toItemOnUpdate(Item item, ItemUpdateDto itemUpdateDto);

    List<ItemDto> toItemDtoList(List<Item> items);

    List<ItemDtoBookingDatesComments> toItemDtoBookingDatesComments(List<Item> items,
                                                                    Map<Long, LocalDateTime> lastBookingDates,
                                                                    Map<Long, LocalDateTime> nearestBookingDates,
                                                                    Map<Long, List<CommentDto>> commentsDtoMap);

    ItemDtoBookingDatesComments toItemDtoWithComments(Item item, List<CommentDto> comments);

    List<ItemResponseDto> toItemResponseDtoList(List<Item> items);
}
