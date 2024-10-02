package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;

/**
 * Сервис работы с вещами.
 */
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    /**
     * Хранилище вещей.
     */
    private final ItemRepository itemRepository;

    /**
     * Хранилище пользователей.
     */
    private final UserRepository userRepository;

    /**
     * Преобразователь данных ввода-вывода.
     */
    private final ItemMapperImpl itemMapper;

    /**
     * Метод добавления вещи.
     * @param itemCreateDto входные данные по вещи для добавления.
     * @return Выходные данные по добавленной вещи.
     */
    @Override
    public ItemDto add(ItemCreateDto itemCreateDto) {
        User owner = checkUserId(itemCreateDto.getUserId());
        Item newItem = itemMapper.toItemOnCreate(itemCreateDto, owner);
        newItem = itemRepository.add(newItem);

        return itemMapper.toItemDto(newItem);
    }

    /**
     * Метод изменения вещи.
     * @param itemUpdateDto входные данные по вещи для изменения.
     * @return Выходные данные по измененной вещи.
     */
    public ItemDto update(ItemUpdateDto itemUpdateDto) {
        long userId = itemUpdateDto.getUserId();
        checkUserId(userId);
        Item itemForUpdate = itemRepository.getById(itemUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена по id = " + itemUpdateDto.getId()));
        if (!itemForUpdate.getOwner().getId().equals(userId)) {
            throw new ConflictException("Вещь не принадлежит пользователю с id = " + userId);
        }

        itemMapper.toItemOnUpdate(itemForUpdate, itemUpdateDto);
        itemForUpdate = itemRepository.update(itemForUpdate);

        return itemMapper.toItemDto(itemForUpdate);
    }

    /**
     * Метод получения данных по вещи.
     * @param itemId идентификатор вещи.
     * @return Выходные данные по вещи.
     */
    @Override
    public ItemDto getById(long itemId) {
        Item item = itemRepository.getById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена по itemId = " + itemId));

        return itemMapper.toItemDto(item);
    }

    /**
     * Метод поиска всех вещей владельца.
     * @param userId идентификатор пользователя-владельца.
     * @return Список выходных данных по вещам.
     */
    @Override
    public List<ItemDto> findAllByOwnerId(long userId) {
        checkUserId(userId);

        return itemMapper.toItemDtoList(itemRepository.findAllByOwnerId(userId));
    }

    /**
     * Метод поиска вещей по тексту наименования и описания.
     * @param userId идентификатор пользователя, выполняющего поиск.
     * @return Список выходных данных по вещам.
     */
    @Override
    public List<ItemDto> search(String text, long userId) {
        checkUserId(userId);

        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemMapper.toItemDtoList(itemRepository.search(text));
    }

    /**
     * Метод проверки наличия указанного пользователя в хранилище.
     *
     * @param userId проверяемый идентификатор пользователя.
     */
    private User checkUserId(long userId) {
        if (userId == 0) {
            throw new ValidationException("userId должен быть указан.");
        }
        return userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден по userId = " + userId));
    }
}
