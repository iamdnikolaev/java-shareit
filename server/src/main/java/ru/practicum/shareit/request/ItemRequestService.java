package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto add(ItemRequestCreateDto itemRequestCreateDto);

    List<ItemRequestDto> getRequestsByRequestorId(long requestorId);

    List<ItemRequestDto> getRequestsByOtherUsers(long requestorId);

    ItemRequestDto getById(long userId, long requestId);
}
