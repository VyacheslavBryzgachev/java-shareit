package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class DbItemStorage {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    public Optional<Item> getItemById(long itemId) {
        return itemRepository.findById(itemId);
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> searchItemByText(String text) {
        return itemRepository.searchItemsByText(text);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
