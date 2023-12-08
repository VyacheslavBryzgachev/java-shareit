package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemRequestTest {
    @Mock
    RequestService requestService;

    @InjectMocks
    ItemRequestController itemRequestController;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createTest() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto
                .builder()
                .id(2)
                .requester(1)
                .description("Описание")
                .build();
        when(requestService.create(any(), anyLong()))
                .thenReturn(itemRequestDto);
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(status().isOk());
        verify(requestService, times(1)).create(any(), anyLong());
    }

    @Test
    void getByIdTest() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto
                .builder()
                .id(2)
                .requester(1)
                .description("Описание")
                .build();
        when(requestService.getById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);
        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
        verify(requestService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    void getAllTest() throws Exception {
        ItemRequestDto itemRequestDto1 = ItemRequestDto
                .builder()
                .id(1)
                .requester(1)
                .description("Описание1")
                .build();
        ItemRequestDto itemRequestDto2 = ItemRequestDto
                .builder()
                .id(2)
                .requester(2)
                .description("Описание2")
                .build();
        List<ItemRequestDto> requestDtos = Arrays.asList(itemRequestDto1, itemRequestDto2);
        when(requestService.getAll(anyLong()))
                .thenReturn(requestDtos);
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(jsonPath("$.size()").value(requestDtos.size()))
                .andExpect(status().isOk());
        verify(requestService, times(1)).getAll(anyLong());
    }

    @Test
    void getAllFromAnotherUserTest() throws Exception {
        ItemRequestDto itemRequestDto1 = ItemRequestDto
                .builder()
                .id(1)
                .requester(1)
                .description("Описание1")
                .build();
        ItemRequestDto itemRequestDto2 = ItemRequestDto
                .builder()
                .id(2)
                .requester(2)
                .description("Описание2")
                .build();
        List<ItemRequestDto> requestDtos = Arrays.asList(itemRequestDto1, itemRequestDto2);
        when(requestService.getAllFromAnotherUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(requestDtos);
        mockMvc.perform(get("/requests/all")
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(1))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(jsonPath("$.size()").value(requestDtos.size()))
                .andExpect(status().isOk());
        verify(requestService, times(1)).getAllFromAnotherUser(anyLong(), anyInt(), anyInt());
    }
}
