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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestTest {
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

        Item item1 = Item
                .builder()
                .id(1)
                .name("Item1")
                .description("Item1Desc")
                .available(true)
                .build();
        Item item2 = Item
                .builder()
                .id(2)
                .name("Item2")
                .description("Item2Desc")
                .available(false)
                .build();
        List<Item> items = Arrays.asList(item1, item2);
        ItemRequestDto itemRequestDto = ItemRequestDto
                .builder()
                .description("Хотел бы воспользоваться щёткой для обуви")
                .items(items)
                .created(LocalDateTime.now())
                .build();

        ItemRequest itemRequest = ItemRequest
                .builder()
                .id(1)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .itemId(1)
                .requester(User
                        .builder()
                        .id(1)
                        .build())
                .createdTime(LocalDateTime.now())
                .build();

        when(requestService.create(any(), anyLong()))
                .thenReturn(itemRequest);
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(status().isOk());
        verify(requestService, times(1)).create(any(), anyLong());
    }

}
