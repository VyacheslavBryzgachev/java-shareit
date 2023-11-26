package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllUsersTest() throws Exception {
        UserDto user1 = UserDto
                .builder()
                .id(1)
                .name("User1")
                .email("user1@mail.com")
                .build();
        UserDto user2 = UserDto
                .builder()
                .id(2)
                .name("User2")
                .email("user2@mail.com")
                .build();
        List<UserDto> userDtos = Arrays.asList(user1, user2);
        when(userService.getAllUsers())
                .thenReturn(userDtos);
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$.size()").value(userDtos.size()))
                .andExpect(status().isOk());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserByIdTest() throws Exception {
        UserDto user = UserDto.builder()
                .id(1)
                .name("User1")
                .email("user1@mail.com")
                .build();
        when(userService.getUserById(anyLong()))
                .thenReturn(user);
        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(status().isOk());
        verify(userService, times(1)).getUserById(anyLong());
    }

    @Test
    void createUserTest() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .id(1)
                .name("User1")
                .email("User1@mail.ru")
                .build();
        User user = User
                .builder()
                .id(1)
                .name("User1")
                .email("User1@mail.ru")
                .build();
        when(userService.createUser(any()))
                .thenReturn(user);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(status().isOk());
        verify(userService, times(1)).createUser(any());
    }

    @Test
    void updateUserTest() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .id(1)
                .name("User1")
                .email("User1@mail.ru")
                .build();
        User user = User
                .builder()
                .id(1)
                .name("User1")
                .email("User1@mail.ru")
                .build();
        when(userService.updateUser(any(), anyLong()))
                .thenReturn(user);
        mockMvc.perform(patch("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(status().isOk());
        verify(userService, times(1)).updateUser(any(), anyLong());
    }

    @Test
    void deleteUserTest() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUser(anyLong());
    }
}
