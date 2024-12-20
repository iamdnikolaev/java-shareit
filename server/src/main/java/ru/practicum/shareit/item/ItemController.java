package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingDatesComments;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

import static ru.practicum.shareit.util.Const.SHARER_USER_ID;

/**
 * Контроллер обработки запросов по обращению с вещами.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    /**
     * Контроллер http-запросов по работе с вещами.
     */
    private final ItemService itemService;

    /**
     * Метод добавления вещи.
     *
     * @param userId        идентификатор добавляющего вещь пользователя;
     * @param itemCreateDto атрибуты вещи для добавления.
     * @return Данные по добавленной вещи.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDto add(@RequestHeader(SHARER_USER_ID) long userId,
                       @RequestBody ItemCreateDto itemCreateDto) {
        log.info("==> add userId = {}, itemCreateDto = {}", userId, itemCreateDto);
        itemCreateDto.setUserId(userId);
        ItemDto itemDto = itemService.add(itemCreateDto);
        log.info("<== {}", itemDto);

        return itemDto;
    }

    /**
     * Метод изменения вещи.
     *
     * @param userId        идентификатор изменяющего вещь пользователя;
     * @param itemId        идентификатор вещи для изменения;
     * @param itemUpdateDto атрибуты вещи для изменения.
     * @return Данные по измененной вещи.
     */
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(SHARER_USER_ID) long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemUpdateDto itemUpdateDto) {
        log.info("==> update userId = {}, itemId = {}, itemUpdateDto = {}", userId, itemId, itemUpdateDto);
        itemUpdateDto.setId(itemId);
        itemUpdateDto.setUserId(userId);
        ItemDto itemDto = itemService.update(itemUpdateDto);
        log.info("<== {}", itemDto);

        return itemDto;
    }

    /**
     * Метод получения данных по вещи.
     *
     * @param userId идентификатор пользователя для получения данных;
     * @param itemId идентификатор вещи для запроса;
     * @return Данные по найденной вещи.
     */
    @GetMapping("/{itemId}")
    public ItemDtoBookingDatesComments get(@RequestHeader(SHARER_USER_ID) long userId, @PathVariable long itemId) {
        log.info("==> get by userId = {}, itemId = {}", userId, itemId);
        ItemDtoBookingDatesComments itemDto = itemService.getById(itemId);
        log.info("<== {}", itemDto);

        return itemDto;
    }

    /**
     * Метод получения вещей владельца.
     *
     * @param userId идентификатор владельца для получения данных;
     * @return Список данных по вещам.
     */
    @GetMapping
    public List<ItemDtoBookingDatesComments> findAllByOwnerId(@RequestHeader(SHARER_USER_ID) long userId) {
        log.info("==> findAllByOwnerId by userId = {}", userId);
        List<ItemDtoBookingDatesComments> itemsDto = itemService.findAllByOwnerId(userId);
        log.info("<== {}", itemsDto);

        return itemsDto;
    }

    /**
     * Метод поиска вещей, доступных для аренды, по названию и/или описанию.
     *
     * @param userId идентификатор владельца для получения данных;
     * @param text   строка поиска.
     * @return Список данных по вещам.
     */
    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(SHARER_USER_ID) long userId, @RequestParam String text) {
        log.info("==> search by userId = {}, text = {}", userId, text);
        List<ItemDto> itemsDto = itemService.search(text, userId);
        log.info("<== {}", itemsDto);

        return itemsDto;
    }

    /**
     * Метод добавления отзыва о вещи после аренды.
     *
     * @param userId           идентификатор пользователя;
     * @param itemId           идентификатор вещи;
     * @param commentCreateDto атрибуты отзыва для добавления.
     * @return Данные по добавленному отзыву.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(SHARER_USER_ID) long userId,
                                 @PathVariable long itemId,
                                 @RequestBody CommentCreateDto commentCreateDto) {
        log.info("==> addComment by userId = {}, itemId = {}, commentCreateDto = {}", userId, itemId, commentCreateDto);
        commentCreateDto.setItemId(itemId);
        commentCreateDto.setAuthorId(userId);
        CommentDto commentDto = itemService.addComment(commentCreateDto);
        log.info("<== {}", commentDto);

        return commentDto;

    }
}
