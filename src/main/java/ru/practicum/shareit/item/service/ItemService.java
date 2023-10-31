package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(int itemId, int userId);

    List<ItemDto> getAllItems(int userId);

    List<ItemDto> searchItemByText(String text, int userId);

    ItemDto createItem(ItemDto itemDto, int userId);

    ItemDto updateItem(ItemDto itemDto, int itemId, int userId);

}
