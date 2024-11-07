package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingDatesComments;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRequestService itemRequestService;

    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();

        userDto = userService.add(new UserCreateDto("commonUser", "commonUser@mail.org"));
    }

    @Test
    void addNewItem() {
        ItemRequestDto requestDto = itemRequestService.add(new ItemRequestCreateDto("Need some big instrument",
                userDto.getId()));

        ItemDto itemDto = itemService.add(new ItemCreateDto("Test new item", "Test new item description",
                true, requestDto.getId(), userDto.getId()));

        assertThat(itemDto).isNotNull();
        assertThat(itemDto.getId()).isNotNull();
        assertThat(itemDto.getName()).isEqualTo("Test new item");
        assertThat(itemDto.getDescription()).isEqualTo("Test new item description");
        assertTrue(itemDto.getAvailable());
        assertEquals(requestDto.getId(), itemDto.getRequestId(), "Wrong requestId");
    }

    @Test
    void addNewItemWrongRequest() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("Test new item", "Test new item description",
                true, 1234L, userDto.getId());

        assertThrows(NotFoundException.class, () -> {
            itemService.add(itemCreateDto);
        }, "Testing wrong requestId failed");
    }

    @Test
    void update() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("Test new item", "Test new item description",
                true, null, userDto.getId());
        ItemDto itemDto = itemService.add(itemCreateDto);

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto(itemDto.getId(), "Test new item updated",
                "Test new item description updated", false, userDto.getId());

        ItemDto itemDtoUpdated = itemService.update(itemUpdateDto);

        assertThat(itemDtoUpdated).isNotNull();
        assertEquals(itemDto.getId(), itemDtoUpdated.getId(), "itemId has changed");
        assertThat(itemDtoUpdated.getName()).isEqualTo("Test new item updated");
        assertThat(itemDtoUpdated.getDescription()).isEqualTo("Test new item description updated");
        assertFalse(itemDtoUpdated.getAvailable());
        assertEquals(0L, itemDtoUpdated.getRequestId(), "Wrong requestId");
    }

    @Test
    void updateWrongUserId() {
        UserDto wrongUser = userService.add(new UserCreateDto("wrongUser", "wrongUser@mail.org"));
        ItemCreateDto itemCreateDto = new ItemCreateDto("Test new item", "Test new item description",
                true, null, userDto.getId());
        ItemDto itemDto = itemService.add(itemCreateDto);

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto(itemDto.getId(), "Test new item updated",
                "Test new item description updated", false, wrongUser.getId());

        assertThrows(ConflictException.class, () -> {
            itemService.update(itemUpdateDto);
        }, "Testing wrong userId for item update failed");
    }

    @Test
    void getById() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("Test new item", "Test new item description",
                true, null, userDto.getId());
        ItemDto itemDto = itemService.add(itemCreateDto);

        ItemDtoBookingDatesComments itemDtoFrom = itemService.getById(itemDto.getId());

        assertThat(itemDtoFrom).isNotNull();
        assertThat(itemDtoFrom.getId()).isNotNull();
        assertThat(itemDtoFrom.getName()).isEqualTo("Test new item");
        assertThat(itemDtoFrom.getDescription()).isEqualTo("Test new item description");
        assertTrue(itemDtoFrom.getAvailable());
        assertEquals(0L, itemDtoFrom.getRequestId(), "Wrong requestId");
    }

    @Test
    void getByIdZero() {
        assertThrows(ValidationException.class, () -> {
            itemService.getById(0L);
        }, "Item cannot be found by id = 0");

    }

    @Test
    void getByIdWrong() {
        assertThrows(NotFoundException.class, () -> {
            itemService.getById(99999L);
        }, "Item cannot be found by id = 99999");
    }

    @Test
    void findAllByOwnerId() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("Test new item", "Test new item description",
                true, null, userDto.getId());
        ItemCreateDto itemCreateDto2 = new ItemCreateDto("Test new item2", "Test new item description2",
                true, null, userDto.getId());

        itemService.add(itemCreateDto);
        itemService.add(itemCreateDto2);

        List<ItemDtoBookingDatesComments> items = itemService.findAllByOwnerId(userDto.getId());

        assertThat(items).hasSize(2);
    }

    @Test
    void findAllEmptyByOwnerId() {
        List<ItemDtoBookingDatesComments> items = itemService.findAllByOwnerId(userDto.getId());

        assertThat(items).hasSize(0);
    }

    @Test
    void findAllByOwnerIdZero() {
        assertThrows(ValidationException.class, () -> {
            itemService.findAllByOwnerId(0L);
        }, "Owner cannot be found by id = 0");

    }

    @Test
    void findAllByOwnerIdWrong() {
        assertThrows(NotFoundException.class, () -> {
            itemService.findAllByOwnerId(1234L);
        }, "Owner cannot be found by id = 1234");
    }

    @Test
    void findAllByOwnerIdItemsBookingsComments() {
        ItemDto item = itemService.add(new ItemCreateDto("Test new item", "Test new item description",
                true, null, userDto.getId()));
        ItemDto item2 = itemService.add(new ItemCreateDto("Test new item2", "Test new item description2",
                true, null, userDto.getId()));
        ItemDto item3 = itemService.add(new ItemCreateDto("Test new item3", "Test new item description3",
                true, null, userDto.getId()));

        UserDto booker = userService.add(new UserCreateDto("NewBooker", "newbooker@google.com"));
        BookingDto booking = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2), item.getId(), booker.getId()));
        bookingService.approve(userDto.getId(), booking.getId(), true);

        BookingDto booking1 = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(2),
                LocalDateTime.now().plusSeconds(3), item2.getId(), booker.getId()));
        bookingService.approve(userDto.getId(), booking1.getId(), true);

        UserDto booker2 = userService.add(new UserCreateDto("NewBooker2", "newbooker2@google.com"));
        BookingDto booking2 = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(3),
                LocalDateTime.now().plusSeconds(4), item2.getId(), booker2.getId()));
        bookingService.approve(userDto.getId(), booking2.getId(), true);

        BookingDto booking3 = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(3),
                LocalDateTime.now().plusSeconds(4), item3.getId(), booker2.getId()));
        bookingService.approve(userDto.getId(), booking3.getId(), true);

        BookingDto booking4 = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(4),
                LocalDateTime.now().plusSeconds(5), item.getId(), booker2.getId()));
        bookingService.approve(userDto.getId(), booking4.getId(), true);

        UserDto booker3 = userService.add(new UserCreateDto("NewBooker3", "newbooker3@google.com"));
        BookingDto booking5 = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(2),
                LocalDateTime.now().plusSeconds(3), item.getId(), booker3.getId()));
        bookingService.approve(userDto.getId(), booking5.getId(), true);

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException ie) {
            System.out.println("Error while waiting 4 seconds.");
        }

        itemService.addComment(new CommentCreateDto("Item works fine", item.getId(), booker.getId()));
        itemService.addComment(new CommentCreateDto("Item works fine again", item.getId(), booker3.getId()));
        itemService.addComment(new CommentCreateDto("Item2 works fine", item2.getId(), booker2.getId()));

        List<ItemDtoBookingDatesComments> items = itemService.findAllByOwnerId(userDto.getId());

        assertThat(items).hasSize(3);
    }

    @Test
    void findAllByOwnerIdNearestBooking() {
        ItemDto item = itemService.add(new ItemCreateDto("Test new item", "Test new item description",
                true, null, userDto.getId()));

        UserDto booker = userService.add(new UserCreateDto("NewBooker", "newbooker@google.com"));
        BookingDto booking = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2), item.getId(), booker.getId()));
        bookingService.approve(userDto.getId(), booking.getId(), true);

        UserDto booker2 = userService.add(new UserCreateDto("NewBooker2", "newbooker2@google.com"));
        BookingDto booking2 = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(2),
                LocalDateTime.now().plusSeconds(3), item.getId(), booker2.getId()));
        bookingService.approve(userDto.getId(), booking2.getId(), true);

        BookingDto booking3 = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(3),
                LocalDateTime.now().plusSeconds(4), item.getId(), booker2.getId()));
        bookingService.approve(userDto.getId(), booking3.getId(), true);

        BookingDto booking4 = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(4),
                LocalDateTime.now().plusSeconds(5), item.getId(), booker2.getId()));
        bookingService.approve(userDto.getId(), booking4.getId(), true);

        UserDto booker3 = userService.add(new UserCreateDto("NewBooker3", "newbooker3@google.com"));
        BookingDto booking5 = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(5),
                LocalDateTime.now().plusSeconds(6), item.getId(), booker3.getId()));
        bookingService.approve(userDto.getId(), booking5.getId(), true);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ie) {
            System.out.println("Error while waiting 3 seconds.");
        }

        List<ItemDtoBookingDatesComments> items = itemService.findAllByOwnerId(userDto.getId());

        assertThat(items).hasSize(1);
    }

    @Test
    void search() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("Test new item", "Test new item description",
                true, null, userDto.getId());
        ItemCreateDto itemCreateDto2 = new ItemCreateDto("Test new item2", "Test new item description2",
                true, null, userDto.getId());

        itemService.add(itemCreateDto);
        itemService.add(itemCreateDto2);

        List<ItemDto> items = itemService.search("item2", userDto.getId());

        assertThat(items).hasSize(1);
        assertEquals("Test new item2", items.getFirst().getName(), "Wrong name for item2");
    }

    @Test
    void searchByEmptyLine() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("Test new item", "Test new item description",
                true, null, userDto.getId());
        ItemCreateDto itemCreateDto2 = new ItemCreateDto("Test new item2", "Test new item description2",
                true, null, userDto.getId());

        itemService.add(itemCreateDto);
        itemService.add(itemCreateDto2);

        List<ItemDto> items = itemService.search("", userDto.getId());

        assertThat(items).hasSize(0);
    }

    @Test
    void addComment() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("Test new item", "Test new item description",
                true, null, userDto.getId());
        ItemCreateDto itemCreateDto2 = new ItemCreateDto("Test new item2", "Test new item description2",
                true, null, userDto.getId());

        ItemDto item = itemService.add(itemCreateDto);
        ItemDto item2 = itemService.add(itemCreateDto2);

        UserDto booker = userService.add(new UserCreateDto("NewBooker", "newbooker@google.com"));
        BookingDto booking = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2), item.getId(), booker.getId()));
        bookingService.approve(userDto.getId(), booking.getId(), true);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ie) {
            System.out.println("Error while waiting 3 seconds.");
        }

        CommentDto commentReady = itemService.addComment(new CommentCreateDto("It works perfectly!", item.getId(), booker.getId()));
        ItemDtoBookingDatesComments itemDtoFrom = itemService.getById(item.getId());

        assertEquals("It works perfectly!", itemDtoFrom.getComments().getFirst().getText(), "Comment has wrong text");
    }

    @Test
    void addCommentCannotBeDone() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("Test new item", "Test new item description",
                true, null, userDto.getId());
        ItemCreateDto itemCreateDto2 = new ItemCreateDto("Test new item2", "Test new item description2",
                true, null, userDto.getId());

        ItemDto item = itemService.add(itemCreateDto);
        ItemDto item2 = itemService.add(itemCreateDto2);

        UserDto booker = userService.add(new UserCreateDto("NewBooker", "newbooker@google.com"));
        BookingDto booking = bookingService.add(new BookingCreateDto(LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(60), item.getId(), booker.getId()));
        bookingService.approve(userDto.getId(), booking.getId(), true);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ie) {
            System.out.println("Error while waiting 3 seconds.");
        }

        assertThrows(ValidationException.class, () -> {
            itemService.addComment(new CommentCreateDto("It works, but I want more", item.getId(), booker.getId()));
        }, "Booker has no rights to make a comment");
    }
}