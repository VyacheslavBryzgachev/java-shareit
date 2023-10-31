package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();
    private final InMemoryUserStorage inMemoryUserStorage;
    private final ItemMapper itemMapper = new ItemMapper();
    private int idCounter = 0;


    public Item getItemById(int itemId, int userId) {
        if (items.get(itemId) != null) {
            return items.get(itemId);
        }
        throw new UnknownIdException("Вещи с таким id=" + itemId + " не найдено");
    }


    public List<Item> getAllItems(int userId) {
        List<Item> itemList = new ArrayList<>();
        User user = inMemoryUserStorage.getUserById(userId);
        for (Item item : items.values()) {
            if (item.getOwner() == user.getId()) {
                itemList.add(item);
            }
        }
        return itemList;
    }


    public List<Item> searchItemByText(String text, int userId) {
        List<Item> searchItems = new ArrayList<>();
        text = text.toLowerCase();
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)) &&
                    item.getAvailable()) {
                searchItems.add(item);
            }
        }
        return searchItems;
    }

    public ItemDto createItem(Item item, int userId) {
        inMemoryUserStorage.getUserById(userId);
        setNextId();
        item.setId(idCounter);
        item.setOwner(userId);
        items.put(item.getId(), item);
        return itemMapper.toItemDto(item);
    }

    public ItemDto updateItem(Item item, int itemId, int userId) {
        List<Item> items = getAllItems(userId);
        for (Item itemToUpd : items) {
            if (itemToUpd.getId() == itemId && itemToUpd.getOwner() == userId) {
                if (item.getAvailable() != null) {
                    itemToUpd.setAvailable(item.getAvailable());
                }
                if (item.getDescription() != null) {
                    itemToUpd.setDescription(item.getDescription());
                }
                if (item.getName() != null) {
                    itemToUpd.setName(item.getName());
                }
                return itemMapper.toItemDto(itemToUpd);
            }
        }
        throw new UnknownIdException("Пользователю c id=" + userId + " не принадлежит вещь с id=" + itemId);
    }

    private void setNextId() {
        idCounter++;
    }
}
