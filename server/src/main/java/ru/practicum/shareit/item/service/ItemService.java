package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(long itemId, Integer userId);

    List<ItemDto> getAllItems(int userId, Integer from, Integer size);

    List<ItemDto> searchItemByText(String text, Integer userId, Integer from, Integer size);

    Item createItem(ItemDto itemDto, Integer userId);

    Item updateItem(ItemDto itemDto, long itemId, Integer userId);

    Comment createComment(CommentDto commentDto, long itemId, Integer userId);

}
