package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(long itemId, int userId);

    List<ItemDto> getAllItems(int userId, Integer from, Integer size);

    List<ItemDto> searchItemByText(String text, int userId, Integer from, Integer size);

    Item createItem(ItemDto itemDto, int userId);

    Item updateItem(ItemDto itemDto, long itemId, int userId);

    Comment createComment(CommentDto commentDto, long itemId, long userId);

}
