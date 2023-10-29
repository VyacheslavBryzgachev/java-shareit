package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final List<Item> items = new ArrayList<>();
    private final ItemMapper itemMapper = new ItemMapper();
    private int idCounter = 0;

    @Override
    public ItemDto getItemById(int itemId, int userId) {
        for (Item item : items) {
            if (item.getId() == itemId) {
                item.setOwner(userId);
                return itemMapper.toItemDto(item);
            }
        }
        throw new UnknownIdException("Вещи с таким id=" + itemId + " не найдено");
    }

    @Override
    public List<Item> getAllItems(int userId) {
        return userService.getUserById(userId).getUsersItems();
    }

    @Override
    public List<Item> searchItemByText(String text, int userId) {
        List<Item> searchItems = new ArrayList<>();
        text = text.toLowerCase();
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        for (Item item : items) {
            if ((item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)) &&
                    item.getAvailable()) {
                searchItems.add(item);
            }
        }
        return searchItems;
    }

    @Override
    public ItemDto createItem(Item item, int userId) {
        User user = fountOwnerItem(userId);
        setNextId();
        item.setId(idCounter);
        item.setOwner(userId);
        user.getUsersItems().add(item);
        items.add(item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Item item, int itemId, int userId) {
        List<Item> items = getAllItems(userId);
        for (Item i : items) {
            if (i.getId() == itemId) {
                if (item.getAvailable() != null) {
                    i.setAvailable(item.getAvailable());
                }
                if (item.getDescription() != null) {
                    i.setDescription(item.getDescription());
                }
                if (item.getName() != null) {
                    i.setName(item.getName());
                }
                return itemMapper.toItemDto(i);
            }
        }
        throw new UnknownIdException("Пользователю c id=" + userId + " не принадлежит вещь с id=" + itemId);
    }

    private User fountOwnerItem(int userId) {
        return userService.getUserById(userId);
    }

    private void setNextId() {
        idCounter++;
    }
}
