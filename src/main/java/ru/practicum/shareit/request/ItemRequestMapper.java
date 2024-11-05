package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRequestMapper {
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemResponseDto> items);

    ItemRequest toItemRequestOnCreate(ItemRequestCreateDto itemRequestCreateDto, User requestor);
}
