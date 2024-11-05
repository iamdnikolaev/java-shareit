package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
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
import java.util.stream.Collectors;

/**
 * Преобразователь данных ввода-вывода по вещам.
 */

@Component
public class ItemMapperImpl implements ItemMapper {

    /**
     * Метод преобразования выходных данных по вещи.
     *
     * @param item вещь для вывода.
     * @return Объект для вывода данных по вещи.
     */
    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : 0)
                .build();
    }

    /**
     * Метод использования входных данных при создании вещи.
     *
     * @param itemCreateDto объект с данными для создания;
     * @param owner         пользователь-владелец вещи.
     * @return Объект вещи после создания.
     */
    public Item toItemOnCreate(ItemCreateDto itemCreateDto, User owner) {
        return Item.builder()
                .name(itemCreateDto.getName())
                .description(itemCreateDto.getDescription())
                .available(itemCreateDto.getAvailable())
                .owner(owner)
                .build();
    }

    /**
     * Метод использования входных данных при изменении вещи.
     *
     * @param item          описание вещи для изменения;
     * @param itemUpdateDto объект с данными для изменения.
     */
    public void toItemOnUpdate(Item item, ItemUpdateDto itemUpdateDto) {
        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        }
    }

    /**
     * Метод преобразования выходных данных по вещам.
     *
     * @param items список вещей для вывода.
     * @return Список объектов для вывода данных.
     */
    public List<ItemDto> toItemDtoList(List<Item> items) {
        return items.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    /**
     * Метод преобразования выходных данных по вещам с указанием дат последнего и ближайшего
     * бронирования
     *
     * @param items               список вещей для вывода;
     * @param lastBookingDates    даты последних броней по вещам;
     * @param nearestBookingDates даты ближайших броней по вещам:
     * @param commentsDtoMap      отзывы о вещах.
     * @return Список объектов для вывода данных.
     */
    public List<ItemDtoBookingDatesComments> toItemDtoBookingDatesComments(List<Item> items,
                                                                           Map<Long, LocalDateTime> lastBookingDates,
                                                                           Map<Long, LocalDateTime> nearestBookingDates,
                                                                           Map<Long, List<CommentDto>> commentsDtoMap) {

        return items.stream()
                .map(item -> ItemDtoBookingDatesComments.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .available(item.getAvailable())
                        .requestId(item.getRequest() != null ? item.getRequest().getId() : 0)
                        .lastBooking(lastBookingDates.get(item.getId()))
                        .nextBooking(nearestBookingDates.get(item.getId()))
                        .comments(commentsDtoMap.get(item.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Метод преобразования выходных данных по вещи с указанием отзывов о ней.
     *
     * @param item     вещь для вывода;
     * @param comments список отзывов о вещи.
     * @return Объект для вывода данных.
     */
    public ItemDtoBookingDatesComments toItemDtoWithComments(Item item, List<CommentDto> comments) {
        return ItemDtoBookingDatesComments.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : 0)
                .comments(comments)
                .build();
    }

    /**
     * Метод преобразования выходных данных о вещах по запросам их предоставления.
     *
     * @param items список вещей для вывода;
     * @return Список объектов вещей для вывода данных.
     */
    public List<ItemResponseDto> toItemResponseDtoList(List<Item> items) {
        return items.stream()
                .map(item -> ItemResponseDto.builder()
                        .itemId(item.getId())
                        .name(item.getName())
                        .ownerId(item.getOwner().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
