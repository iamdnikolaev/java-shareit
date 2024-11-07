package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import static ru.practicum.shareit.util.Const.SHARER_USER_ID;

/**
 * Контроллер обработки запросов вещей.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    /**
     * Контроллер обработки http-запросов по обращению с запросами вещей.
     */
    private final ItemRequestClient itemRequestClient;

    /**
     * Метод добавления запроса вещи.
     *
     * @param userId               идентификатор добавляющего вещь пользователя;
     * @param itemRequestCreateDto атрибуты запроса вещи для добавления.
     * @return Данные по добавленному запросу вещи.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                      @Valid @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        log.info("==> add userId = {}, itemRequestCreateDto = {}", userId, itemRequestCreateDto);
        itemRequestCreateDto.setUserId(userId);
        ResponseEntity<Object> itemRequestDto = itemRequestClient.add(itemRequestCreateDto);
        log.info("<== add {}", itemRequestDto);

        return itemRequestDto;
    }

    /**
     * Метод получения своих запросов вещей.
     *
     * @param userId идентификатор пользователя, запросившего вещи.
     * @return Список данных по запросах вещей с ответами.
     */
    @GetMapping
    public ResponseEntity<Object> getRequestsByRequestorId(@RequestHeader(SHARER_USER_ID) @Positive long userId) {
        log.info("==> getRequestsByRequestorId userId = {}", userId);
        ResponseEntity<Object> itemRequestDtos = itemRequestClient.getRequestsByRequestorId(userId);
        log.info("<== getRequestsByRequestorId {}", itemRequestDtos);

        return itemRequestDtos;
    }

    /**
     * Метод получения запросов вещей от других пользователей.
     *
     * @param userId идентификатор пользователя, ищущего запросы.
     * @return Список данных по запросах вещей с ответами.
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsByOtherUsers(@RequestHeader(SHARER_USER_ID) @Positive long userId) {
        log.info("==> getRequestsByOtherUsers userId = {}", userId);
        ResponseEntity<Object> itemRequestDtos = itemRequestClient.getRequestsByOtherUsers(userId);
        log.info("<== getRequestsByOtherUsers {}", itemRequestDtos);

        return itemRequestDtos;
    }

    /**
     * Метод получения данных об одном запросе вещи.
     *
     * @param userId    идентификатор пользователя, ищущего запрос.
     * @param requestId идентификатор интересующего запроса.
     * @return Данные по запросу вещи с ответами на него.
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(SHARER_USER_ID) @Positive long userId,
                                          @PathVariable long requestId) {
        log.info("==> getById userId = {}, requestId = {}", userId, requestId);
        ResponseEntity<Object> itemRequestDto = itemRequestClient.getById(userId, requestId);
        log.info("<== getById {}", itemRequestDto);

        return itemRequestDto;
    }
}
