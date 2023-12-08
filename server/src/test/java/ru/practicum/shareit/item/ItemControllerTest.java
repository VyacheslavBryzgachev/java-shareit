package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    ItemService itemService;

    @InjectMocks
    ItemController itemController;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createItemTest() throws Exception {
        ItemDto itemDto = ItemDto
                .builder()
                .id(1)
                .owner(1)
                .name("Item1")
                .description("Item1Desc")
                .available(true)
                .build();
        Item item = Item
                .builder()
                .id(1)
                .owner(User.builder()
                        .id(1)
                        .build())
                .name("Item1")
                .description("Item1Desc")
                .available(true)
                .build();
        when(itemService.createItem(any(), anyInt()))
                .thenReturn(item);
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(status().isOk());
        verify(itemService, times(1)).createItem(any(), anyInt());
    }

    @Test
    void updateItemTest() throws Exception {
        ItemDto itemDto = ItemDto
                .builder()
                .id(1)
                .name("Item1")
                .description("Item1Desc")
                .available(true)
                .build();
        Item item = Item
                .builder()
                .id(1)
                .owner(User.builder()
                        .id(1)
                        .build())
                .name("Item1")
                .description("Item1Desc")
                .available(true)
                .build();
        when(itemService.updateItem(any(), anyLong(), anyInt()))
                .thenReturn(item);
        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(status().isOk());
        verify(itemService, times(1)).updateItem(any(), anyLong(), anyInt());
    }

    @Test
    void getItemByIdTest() throws Exception {
        ItemDto itemDto = ItemDto
                .builder()
                .id(1)
                .name("Item1")
                .description("Item1Desc")
                .available(true)
                .build();
        when(itemService.getItemById(anyLong(), anyInt()))
                .thenReturn(itemDto);
        mockMvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(status().isOk());
        verify(itemService, times(1)).getItemById(anyLong(), anyInt());
    }

    @Test
    void getAllItemsTest() throws Exception {
        ItemDto itemDto1 = ItemDto
                .builder()
                .id(1)
                .name("Item1")
                .description("Item1Desc")
                .available(true)
                .build();
        ItemDto itemDto2 = ItemDto
                .builder()
                .id(2)
                .name("Item2")
                .description("Item2Desc")
                .available(false)
                .build();
        List<ItemDto> itemDtos = Arrays.asList(itemDto1, itemDto2);
        when(itemService.getAllItems(anyInt(), anyInt(), anyInt()))
                .thenReturn(itemDtos);
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(jsonPath("$.size()").value(itemDtos.size()))
                .andExpect(status().isOk());
        verify(itemService, times(1)).getAllItems(anyInt(), anyInt(), anyInt());
    }

    @Test
    void searchItemByTextTest() throws Exception {
        ItemDto itemDto1 = ItemDto
                .builder()
                .id(1)
                .name("Item1")
                .description("Item1Desc")
                .available(true)
                .build();
        ItemDto itemDto2 = ItemDto
                .builder()
                .id(2)
                .name("Item2")
                .description("Item2Desc")
                .available(false)
                .build();
        List<ItemDto> itemDtos = Arrays.asList(itemDto1, itemDto2);
        when(itemService.searchItemByText(eq("Текст"), anyInt(), anyInt(), anyInt()))
                .thenReturn(itemDtos);
        mockMvc.perform(get("/items/search")
                        .param("text", "Текст")
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(1))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(jsonPath("$.size()").value(itemDtos.size()))
                .andExpect(status().isOk());
        verify(itemService, times(1)).searchItemByText(eq("Текст"), anyInt(), anyInt(), anyInt());
    }

    @Test
    void createCommentTest() throws Exception {
        Comment comment = Comment
                .builder()
                .id(1)
                .text("Комментарий")
                .authorName("Автор")
                .build();
        when(itemService.createComment(any(), anyLong(), anyInt()))
                .thenReturn(comment);
        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(jsonPath("$.id").value(comment.getId()))
                .andExpect(jsonPath("$.text").value(comment.getText()))
                .andExpect(jsonPath("$.authorName").value(comment.getAuthorName()))
                .andExpect(status().isOk());
        verify(itemService, times(1)).createComment(any(), anyLong(), anyInt());
    }
}
