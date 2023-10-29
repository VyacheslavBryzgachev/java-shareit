package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(int itemId, int userId);

    List<Item> getAllItems(int userId);

    List<Item> searchItemByText(String text, int userId);

    ItemDto createItem(Item item, int userId);

    ItemDto updateItem(Item item, int itemId, int userId);

}
