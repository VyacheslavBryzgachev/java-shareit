package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.InMemoryItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final InMemoryItemStorage inMemoryItemStorage;
    private final ItemMapper itemMapper = new ItemMapper();

    @Override
    public ItemDto getItemById(int itemId, int userId) {
        return itemMapper.toItemDto(inMemoryItemStorage.getItemById(itemId, userId));
    }

    @Override
    public List<ItemDto> getAllItems(int userId) {
        List<Item> itemList = inMemoryItemStorage.getAllItems(userId);
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(itemMapper.toItemDto(item));
        }
        return itemDtoList;
    }

    @Override
    public List<ItemDto> searchItemByText(String text, int userId) {
        List<Item> itemList = inMemoryItemStorage.searchItemByText(text, userId);
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(itemMapper.toItemDto(item));
        }
        return itemDtoList;
    }

    @Override
    public Item createItem(ItemDto itemDto, int userId) {
        return inMemoryItemStorage.createItem(itemMapper.toItem(itemDto), userId);
    }

    @Override
    public Item updateItem(ItemDto itemDto, int itemId, int userId) {
        return inMemoryItemStorage.updateItem(itemMapper.toItem(itemDto), itemId, userId);
    }

}
