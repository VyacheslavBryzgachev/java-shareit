package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.DbItemStorage;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper = new ItemMapper();
    private final CommentMapper commentMapper = new CommentMapper();
    private final DbItemStorage dbItemStorage;

    @Override
    public ItemDto getItemById(long itemId, int userId) {
        return dbItemStorage.getItemById(itemId, userId);
    }

    @Override
    public List<ItemDto> getAllItems(int userId) {
        return dbItemStorage.gelAllItems(userId);
    }

    @Override
    public List<ItemDto> searchItemByText(String text, int userId) {
        List<Item> itemList = dbItemStorage.searchItemByText(text, userId);
        return itemList.stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, int userId) {
        itemDto.setOwner(userId);
        return dbItemStorage.createItem(itemMapper.toItem(itemDto), userId);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, int userId) {
        return dbItemStorage.updateItem(itemMapper.toItem(itemDto), itemId, userId);
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, long itemId, long userId) {
        return commentMapper.toCommentDto(dbItemStorage.createComment(commentDto, itemId, userId));
    }
}
