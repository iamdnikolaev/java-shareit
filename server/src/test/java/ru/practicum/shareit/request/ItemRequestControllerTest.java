package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ItemRequestControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private ItemRequestService itemRequestService;

    public static final long RESULT_ID_TEST = 1L;
    public static final long USER_ID_TEST = 1L;
    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void addRequest() throws Exception {
        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto("Дрель с ударным механизмом (1000 Вт)",
                USER_ID_TEST);

        ItemRequestDto itemRequestDto = new ItemRequestDto(RESULT_ID_TEST, "Дрель с ударным механизмом (1000 Вт)",
                LocalDateTime.now(), null);

        when(itemRequestService.add(itemRequestCreateDto)).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header(SHARER_USER_ID, USER_ID_TEST)
                        .content(mapper.writeValueAsString(itemRequestCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(itemRequestService, times(1)).add(any());
    }

    @Test
    void getRequestsByRequestorId() throws Exception {
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        when(itemRequestService.getRequestsByRequestorId(USER_ID_TEST)).thenReturn(itemRequestDtos);

        mockMvc.perform(get("/requests")
                        .header(SHARER_USER_ID, USER_ID_TEST)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).getRequestsByRequestorId(USER_ID_TEST);
    }

    @Test
    void getRequestsByOtherUsers() throws Exception {
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();

        when(itemRequestService.getRequestsByOtherUsers(USER_ID_TEST)).thenReturn(itemRequestDtos);

        mockMvc.perform(get("/requests/all")
                        .header(SHARER_USER_ID, USER_ID_TEST)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).getRequestsByOtherUsers(USER_ID_TEST);
    }

    @Test
    void getById() throws Exception {
        when(itemRequestService.getById(USER_ID_TEST, RESULT_ID_TEST)).thenReturn(new ItemRequestDto());

        mockMvc.perform(get("/requests/{requestId}", RESULT_ID_TEST)
                        .header(SHARER_USER_ID, USER_ID_TEST)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).getById(USER_ID_TEST, RESULT_ID_TEST);
    }
}