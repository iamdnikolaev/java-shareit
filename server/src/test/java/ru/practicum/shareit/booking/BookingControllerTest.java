package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BookingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private BookingService bookingService;

    public static final long RESULT_ID_TEST = 1L;
    public static final long USER_ID_TEST = 1L;
    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Test
    void addBooking() throws Exception {
        BookingCreateDto bookingCreateDto = new BookingCreateDto(LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2), RESULT_ID_TEST, USER_ID_TEST);

        BookingDto bookingDto = new BookingDto(RESULT_ID_TEST, LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2), BookingStatus.WAITING, new ItemDto(), new UserDto());

        when(bookingService.add(bookingCreateDto)).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header(SHARER_USER_ID, USER_ID_TEST)
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(bookingService, times(1)).add(any());
    }

    @Test
    void approveBooking() throws Exception {
        when(bookingService.approve(USER_ID_TEST, RESULT_ID_TEST, true)).thenReturn(new BookingDto());

        mockMvc.perform(patch("/bookings/{bookingId}", RESULT_ID_TEST)
                        .param("approved", "true")
                        .header(SHARER_USER_ID, USER_ID_TEST))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).approve(USER_ID_TEST, RESULT_ID_TEST, true);
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getById(RESULT_ID_TEST, USER_ID_TEST)).thenReturn(new BookingDto());

        mockMvc.perform(get("/bookings/{bookingId}", RESULT_ID_TEST)
                        .header(SHARER_USER_ID, USER_ID_TEST))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getById(RESULT_ID_TEST, USER_ID_TEST);

    }

    @Test
    void getBookingsByBooker() throws Exception {
        when(bookingService.getBookingsByBooker(USER_ID_TEST, BookingState.ALL)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header(SHARER_USER_ID, USER_ID_TEST))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getBookingsByBooker(USER_ID_TEST, BookingState.ALL);
    }

    @Test
    void getBookingsByOwner() throws Exception {
        when(bookingService.getBookingsByOwner(USER_ID_TEST, BookingState.ALL))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .header(SHARER_USER_ID, USER_ID_TEST))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1)).getBookingsByOwner(USER_ID_TEST, BookingState.ALL);
    }
}