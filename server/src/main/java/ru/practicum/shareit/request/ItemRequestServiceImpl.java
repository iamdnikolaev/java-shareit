package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    /**
     * Хранилище запросов вещей.
     */
    private final ItemRequestRepository itemRequestRepository;

    /**
     * Хранилище вещей.
     */
    private final ItemRepository itemRepository;

    /**
     * Хранилище пользователей.
     */
    private final UserRepository userRepository;

    /**
     * Преобразователь данных ввода-вывода по запросам вещи.
     */
    private final ItemRequestMapper itemRequestMapper;

    /**
     * Преобразователь данных ввода-вывода по вещам.
     */
    private final ItemMapper itemMapper;

    /**
     * Метод добавления запроса вещи.
     *
     * @param itemRequestCreateDto атрибуты запроса вещи для добавления.
     * @return Данные по добавленному запросу вещи.
     */
    @Override
    @Transactional
    public ItemRequestDto add(ItemRequestCreateDto itemRequestCreateDto) {
        User requestor = checkUserId(itemRequestCreateDto.getUserId());
        ItemRequest newRequest = itemRequestMapper.toItemRequestOnCreate(itemRequestCreateDto, requestor);
        newRequest = itemRequestRepository.save(newRequest);

        return itemRequestMapper.toItemRequestDto(newRequest, Collections.emptyList());
    }

    /**
     * Метод получения своих запросов вещей с ответами на них.
     *
     * @param requestorId идентификатор пользователя, запросившего вещи.
     * @return Список данных по запросах вещей с ответами.
     */
    @Override
    public List<ItemRequestDto> getRequestsByRequestorId(long requestorId) {
        checkUserId(requestorId);

        return getRequestDtos(itemRequestRepository.findByRequestorIdOrderByCreatedDesc(requestorId));
    }

    /**
     * Метод получения запросов вещей от других пользователей.
     *
     * @param requestorId идентификатор пользователя, ищущего запросы.
     * @return Список данных по запросах вещей с ответами.
     */
    @Override
    public List<ItemRequestDto> getRequestsByOtherUsers(long requestorId) {
        checkUserId(requestorId);

        return getRequestDtos(itemRequestRepository.findByOtherUsers(requestorId));
    }

    /**
     * Метод получения данных об одном запросе вещи.
     *
     * @param userId    идентификатор пользователя, ищущего запрос.
     * @param requestId идентификатор интересующего запроса.
     * @return Данные по запросу вещи с ответами на него.
     */
    @Override
    public ItemRequestDto getById(long userId, long requestId) {
        checkUserId(userId);
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос вещи не найден по requestId = " + requestId));

        return itemRequestMapper.toItemRequestDto(request,
                itemMapper.toItemResponseDtoList(itemRepository.getItemsByRequestId(request.getId())));
    }

    /**
     * Метод проверки наличия указанного пользователя в хранилище.
     *
     * @param userId проверяемый идентификатор пользователя.
     * @return Найденный пользователь
     */
    private User checkUserId(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден по userId = " + userId));
    }

    /**
     * Метод получения данных о запросах вещей с описанием самих вещей.
     *
     * @param requests список запросов вещей для обработки.
     * @return Список данных о запросах вещей
     */
    private List<ItemRequestDto> getRequestDtos(List<ItemRequest> requests) {
        List<Long> requestIds = requests.stream().map(ItemRequest::getId).toList();
        List<Item> items = itemRepository.getItemsByRequestIds(requestIds);

        Map<Long, List<ItemResponseDto>> requestItems = new HashMap<>();
        for (Item item : items) {
            List<ItemResponseDto> itemResponseDtos = requestItems.get(item.getRequest().getId());
            if (itemResponseDtos == null) {
                itemResponseDtos = new ArrayList<>();
                itemResponseDtos.add(itemMapper.toItemResponseDto(item));
            } else {
                itemResponseDtos.add(itemMapper.toItemResponseDto(item));
            }
            requestItems.put(item.getRequest().getId(), itemResponseDtos);
        }

        return requests.stream()
                .map(request -> itemRequestMapper.toItemRequestDto(request, requestItems.get(request.getId())))
                .toList();
    }
}
