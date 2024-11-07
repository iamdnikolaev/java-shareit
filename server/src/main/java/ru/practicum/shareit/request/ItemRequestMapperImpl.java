package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ItemRequestMapperImpl implements ItemRequestMapper {

    /**
     * Метод преобразования выходных данных по запросам вещи.
     *
     * @param itemRequest запрос вещи для вывода;
     * @param items       вещи, предлагаемые ответом на обрабатываемый запрос;
     * @return Объект для вывода данных по запросам вещи.
     */
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemResponseDto> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }

    /**
     * Метод использования входных данных при создании запроса вещи.
     *
     * @param itemRequestCreateDto объект с данными для создания запроса вещи;
     * @param requestor            пользователь, запрашивающий вещь для использования.
     * @return Объект запроса вещи.
     */
    public ItemRequest toItemRequestOnCreate(ItemRequestCreateDto itemRequestCreateDto, User requestor) {
        return ItemRequest.builder()
                .description(itemRequestCreateDto.getDescription())
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
    }
}
