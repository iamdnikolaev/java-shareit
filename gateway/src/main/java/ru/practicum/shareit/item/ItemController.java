package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import static ru.practicum.shareit.util.Const.SHARER_USER_ID;

/**
 * Контроллер обработки http-запросов по обращению с вещами.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    /**
     * Клиент для связи сервером по работе с вещами.
     */
    private final ItemClient itemClient;

    /**
     * Метод добавления вещи.
     *
     * @param userId        идентификатор добавляющего вещь пользователя;
     * @param itemCreateDto атрибуты вещи для добавления.
     * @return Данные по добавленной вещи.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                      @Valid @RequestBody ItemCreateDto itemCreateDto) {
        log.info("==> add userId = {}, itemCreateDto = {}", userId, itemCreateDto);
        itemCreateDto.setUserId(userId);
        ResponseEntity<Object> itemDto = itemClient.add(itemCreateDto);
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
    public ResponseEntity<Object> update(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                         @PathVariable @Positive long itemId,
                                         @Valid @RequestBody ItemUpdateDto itemUpdateDto) {
        log.info("==> update userId = {}, itemId = {}, itemUpdateDto = {}", userId, itemId, itemUpdateDto);
        itemUpdateDto.setId(itemId);
        itemUpdateDto.setUserId(userId);
        ResponseEntity<Object> itemDto = itemClient.update(itemUpdateDto);
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
    public ResponseEntity<Object> get(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                      @PathVariable @Positive long itemId) {
        log.info("==> get by userId = {}, itemId = {}", userId, itemId);
        ResponseEntity<Object> itemDto = itemClient.getById(userId, itemId);
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
    public ResponseEntity<Object> findAllByOwnerId(@RequestHeader(SHARER_USER_ID) @Positive long userId) {
        log.info("==> findAllByOwnerId by userId = {}", userId);
        ResponseEntity<Object> itemsDto = itemClient.findAllByOwnerId(userId);
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
    public ResponseEntity<Object> search(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                         @RequestParam String text) {
        log.info("==> search by userId = {}, text = {}", userId, text);
        if (text.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        ResponseEntity<Object> itemsDto = itemClient.search(text, userId);
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
    public ResponseEntity<Object> addComment(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                             @PathVariable @Positive long itemId,
                                             @Valid @RequestBody CommentCreateDto commentCreateDto) {
        log.info("==> addComment by userId = {}, itemId = {}, commentCreateDto = {}", userId, itemId, commentCreateDto);
        commentCreateDto.setItemId(itemId);
        commentCreateDto.setAuthorId(userId);
        ResponseEntity<Object> commentDto = itemClient.addComment(commentCreateDto);
        log.info("<== {}", commentDto);

        return commentDto;
    }
}
