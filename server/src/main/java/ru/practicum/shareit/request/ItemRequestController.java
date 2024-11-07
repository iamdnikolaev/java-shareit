package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

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
     * Контроллер http-запросов по работе с запросами вещей.
     */
    private final ItemRequestService itemRequestService;

    /**
     * Метод добавления запроса вещи.
     *
     * @param userId               идентификатор добавляющего вещь пользователя;
     * @param itemRequestCreateDto атрибуты запроса вещи для добавления.
     * @return Данные по добавленному запросу вещи.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemRequestDto add(@RequestHeader(SHARER_USER_ID) Long userId,
                              @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        log.info("==> add userId = {}, itemRequestCreateDto = {}", userId, itemRequestCreateDto);
        itemRequestCreateDto.setUserId(userId);
        ItemRequestDto itemRequestDto = itemRequestService.add(itemRequestCreateDto);
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
    public List<ItemRequestDto> getRequestsByRequestorId(@RequestHeader(SHARER_USER_ID) Long userId) {
        log.info("==> getRequestsByRequestorId userId = {}", userId);
        List<ItemRequestDto> itemRequestDtos = itemRequestService.getRequestsByRequestorId(userId);
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
    public List<ItemRequestDto> getRequestsByOtherUsers(@RequestHeader(SHARER_USER_ID) Long userId) {
        log.info("==> getRequestsByOtherUsers userId = {}", userId);
        List<ItemRequestDto> itemRequestDtos = itemRequestService.getRequestsByOtherUsers(userId);
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
    public ItemRequestDto getById(@RequestHeader(SHARER_USER_ID) Long userId,
                                  @PathVariable long requestId) {
        log.info("==> getById userId = {}, requestId = {}", userId, requestId);
        ItemRequestDto itemRequestDto = itemRequestService.getById(userId, requestId);
        log.info("<== getById {}", itemRequestDto);

        return itemRequestDto;
    }
}
