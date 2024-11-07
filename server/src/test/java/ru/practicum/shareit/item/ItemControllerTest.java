package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingDatesComments;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
class ItemControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private ItemService itemService;

    public static final long RESULT_ID_TEST = 1L;
    public static final long USER_ID_TEST = 1L;
    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void addItem() throws Exception {
        ItemCreateDto itemCreateDto = new ItemCreateDto("Дрель", "Дрель с ударным механизмом (1000 Вт)",
                true, 0L, USER_ID_TEST);

        ItemDto itemDto = new ItemDto(RESULT_ID_TEST, "Дрель", "Дрель с ударным механизмом (1000 Вт)",
                true, 0L);

        when(itemService.add(itemCreateDto)).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header(SHARER_USER_ID, USER_ID_TEST)
                        .content(mapper.writeValueAsString(itemCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(itemService, times(1)).add(any());
    }

    @Test
    void updateItem() throws Exception {
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto(RESULT_ID_TEST, "Дрель ", "Дрель с ударным механизмом (2000 Вт)",
                true, USER_ID_TEST);

        ItemDto itemDto = new ItemDto(RESULT_ID_TEST, "Дрель", "Дрель с ударным механизмом (2000 Вт)",
                true, 0L);

        when(itemService.update(itemUpdateDto)).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", RESULT_ID_TEST)
                        .header(SHARER_USER_ID, USER_ID_TEST)
                        .content(mapper.writeValueAsString(itemUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService, times(1)).update(any());
    }

    @Test
    void getItem() throws Exception {
        when(itemService.getById(RESULT_ID_TEST)).thenReturn(new ItemDtoBookingDatesComments());

        mockMvc.perform(get("/items/{itemId}", RESULT_ID_TEST)
                        .header(SHARER_USER_ID, USER_ID_TEST)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService, times(1)).getById(RESULT_ID_TEST);
    }

    @Test
    void findAllByOwnerId() throws Exception {
        List<ItemDtoBookingDatesComments> itemsDto = new ArrayList<>();

        when(itemService.findAllByOwnerId(USER_ID_TEST)).thenReturn(itemsDto);

        mockMvc.perform(get("/items")
                        .header(SHARER_USER_ID, USER_ID_TEST)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService, times(1)).findAllByOwnerId(USER_ID_TEST);
    }

    @Test
    void searchItems() throws Exception {
        when(itemService.search("", USER_ID_TEST)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", "")
                        .header(SHARER_USER_ID, USER_ID_TEST)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Collections.emptyList())));

        verify(itemService, times(1)).search("", USER_ID_TEST);
    }

    @Test
    void addComment() throws Exception {
        CommentCreateDto commentCreateDto = new CommentCreateDto("Вещь интересная", RESULT_ID_TEST, USER_ID_TEST);

        when(itemService.addComment(commentCreateDto)).thenReturn(new CommentDto());

        mockMvc.perform(post("/items/{itemId}/comment", RESULT_ID_TEST)
                        .header(SHARER_USER_ID, USER_ID_TEST)
                        .content(mapper.writeValueAsString(commentCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(itemService, times(1)).addComment(any());
    }
}