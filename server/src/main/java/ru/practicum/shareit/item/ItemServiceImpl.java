package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingDatesComments;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис работы с вещами.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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
     * Хранилище бронирований вещей.
     */
    private final BookingRepository bookingRepository;

    /**
     * Хранилище отзывов о вещах.
     */
    private final CommentRepository commentRepository;

    /**
     * Хранилище запросов вещей.
     */
    private final ItemRequestRepository itemRequestRepository;

    /**
     * Преобразователь данных ввода-вывода по вещи.
     */
    private final ItemMapperImpl itemMapper;

    /**
     * Преобразователь данных ввода-вывода по отзывам.
     */
    private final CommentMapperImpl commentMapper;

    /**
     * Метод добавления вещи.
     *
     * @param itemCreateDto входные данные по вещи для добавления.
     * @return Выходные данные по добавленной вещи.
     */
    @Override
    @Transactional
    public ItemDto add(ItemCreateDto itemCreateDto) {
        User owner = checkUserId(itemCreateDto.getUserId());
        Item newItem = itemMapper.toItemOnCreate(itemCreateDto, owner);

        if (itemCreateDto.getRequestId() != null && itemCreateDto.getRequestId() != 0L) {
            ItemRequest request = itemRequestRepository.findById(itemCreateDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос не найден по requestID = "
                            + itemCreateDto.getRequestId()));
            newItem.setRequest(request);
        }

        newItem = itemRepository.save(newItem);

        return itemMapper.toItemDto(newItem);
    }

    /**
     * Метод изменения вещи.
     *
     * @param itemUpdateDto входные данные по вещи для изменения.
     * @return Выходные данные по измененной вещи.
     */
    @Override
    @Transactional
    public ItemDto update(ItemUpdateDto itemUpdateDto) {
        long userId = itemUpdateDto.getUserId();
        checkUserId(userId);
        Item itemForUpdate = checkItemId(itemUpdateDto.getId());
        if (!itemForUpdate.getOwner().getId().equals(userId)) {
            throw new ConflictException("Вещь не принадлежит пользователю с id = " + userId);
        }

        itemMapper.toItemOnUpdate(itemForUpdate, itemUpdateDto);
        itemForUpdate = itemRepository.save(itemForUpdate);

        return itemMapper.toItemDto(itemForUpdate);
    }

    /**
     * Метод получения данных по вещи.
     *
     * @param itemId идентификатор вещи.
     * @return Выходные данные по вещи.
     */
    @Override
    public ItemDtoBookingDatesComments getById(long itemId) {
        Item item = checkItemId(itemId);
        List<Comment> comments = commentRepository.getCommentsByItemIds(List.of(itemId));

        List<CommentDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentsDto.add(commentMapper.toCommentDto(comment));
        }

        return itemMapper.toItemDtoWithComments(item, commentsDto);
    }

    /**
     * Метод поиска всех вещей владельца.
     *
     * @param ownerId идентификатор пользователя-владельца.
     * @return Список выходных данных по вещам.
     */
    @Override
    public List<ItemDtoBookingDatesComments> findAllByOwnerId(long ownerId) {
        checkUserId(ownerId);

        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        if (items != null && !items.isEmpty()) {
            List<Long> itemIds = items.stream().map(Item::getId).toList();

            List<Booking> lastBookings = bookingRepository.lastBookingsByItemIds(itemIds, LocalDateTime.now());
            List<Booking> nearestBookings = bookingRepository.nearestBookingsByItemIds(itemIds, LocalDateTime.now());
            List<Comment> comments = commentRepository.getCommentsByItemIds(itemIds);

            Map<Long, LocalDateTime> lastBookingDates = new HashMap<>();
            for (Booking lastBooking : lastBookings) {
                lastBookingDates.put(lastBooking.getItem().getId(), lastBooking.getStart());
            }

            Map<Long, LocalDateTime> nearestBookingDates = new HashMap<>();
            for (Booking nearestBooking : nearestBookings) {
                nearestBookingDates.put(nearestBooking.getItem().getId(), nearestBooking.getStart());
            }

            Map<Long, List<CommentDto>> commentsDtoMap = new HashMap<>();
            for (Comment comment : comments) {
                List<CommentDto> itemCommentDtos = commentsDtoMap.get(comment.getItem().getId());
                if (itemCommentDtos == null || itemCommentDtos.isEmpty()) {
                    itemCommentDtos = new ArrayList<>();
                    itemCommentDtos.add(commentMapper.toCommentDto(comment));
                } else {
                    itemCommentDtos.add(commentMapper.toCommentDto(comment));
                }
                commentsDtoMap.put(comment.getItem().getId(), itemCommentDtos);
            }

            return itemMapper.toItemDtoBookingDatesComments(items, lastBookingDates, nearestBookingDates, commentsDtoMap);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Метод поиска вещей по тексту наименования и описания.
     *
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
     * Метод добавления отзыва о вещи после аренды.
     *
     * @param commentCreateDto отзыв для добавления.
     * @return Данные по добавленному отзыву.
     */
    @Transactional
    @Override
    public CommentDto addComment(CommentCreateDto commentCreateDto) {
        Item item = checkItemId(commentCreateDto.getItemId());
        User author = checkUserId(commentCreateDto.getAuthorId());

        List<Booking> bookings = bookingRepository.findPastBookingsByBookerIdAndStatus(commentCreateDto.getAuthorId(),
                LocalDateTime.now(),
                List.of(BookingStatus.APPROVED));
        log.info("==> ItemService addComment now() = {} <==", LocalDateTime.now());

        if (bookings == null || bookings.isEmpty()) {
            throw new ValidationException("У пользователя userId = " + commentCreateDto.getAuthorId()
                    + " нет завершенной аренды вещи itemId = " + commentCreateDto.getItemId()
                    + ". Он не может оставлять отзыв.");
        }

        Comment newComment = commentMapper.toCommentOnCreate(commentCreateDto, item, author);
        newComment = commentRepository.save(newComment);

        return commentMapper.toCommentDto(newComment);
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
     * Метод проверки наличия указанной вещи в хранилище.
     *
     * @param itemId идентификатор вещи.
     * @return Найденная вещь
     */
    private Item checkItemId(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена по itemId = " + itemId));
    }
}
