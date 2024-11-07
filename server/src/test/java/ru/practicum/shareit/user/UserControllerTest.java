package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private UserService userService;

    public static final long RESULT_ID_TEST = 1L;
    public static final long USER_ID_TEST = 1L;
    public static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void addUser() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto("Основной", "main@user.com");

        UserDto userDto = new UserDto(USER_ID_TEST, "Основной", "main@user.com");

        when(userService.add(userCreateDto)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(userService, times(1)).add(any());
    }

    @Test
    void update() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto(USER_ID_TEST, "МегаОсновной", "mainest@user.com");

        UserDto userDto = new UserDto(USER_ID_TEST, "МегаОсновной", "mainest@user.com");

        when(userService.update(userUpdateDto)).thenReturn(userDto);

        mockMvc.perform(patch("/users/{userId}", USER_ID_TEST)
                        .content(mapper.writeValueAsString(userUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1)).update(any());
    }

    @Test
    void getUser() throws Exception {
        when(userService.getById(USER_ID_TEST)).thenReturn(new UserDto());

        mockMvc.perform(get("/users/{userId}", USER_ID_TEST)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1)).getById(USER_ID_TEST);
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).delete(USER_ID_TEST);

        mockMvc.perform(delete("/users/{userId}", USER_ID_TEST))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(USER_ID_TEST);
    }
}