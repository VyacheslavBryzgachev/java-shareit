package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DbItemStorage {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public Item getItemById(long itemId, long userId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new UnknownIdException("Вещи таким id=" + itemId + " не найдено"));
    }

    public Item createItem(Item item, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnknownIdException("Пользователя таким id=" + userId + " не найдено"));
        item.setOwner(user);
        return itemRepository.save(item);
    }

    public Item updateItem(Item item, long itemId, int userId) {
        return itemRepository.findById(itemId).filter(item1 -> item1.getOwner().getId() == userId).map(item1 -> {
            if (item.getAvailable() != null) {
                item1.setAvailable(item.getAvailable());
            }
            if (item.getDescription() != null) {
                item1.setDescription(item.getDescription());
            }
            if (item.getName() != null) {
                item1.setName(item.getName());
            }
            return itemRepository.save(item1);
        }).orElseThrow(() -> new UnknownIdException("Вещь не найдена"));
    }

    public List<Item> searchItemByText(String text, int userId) {
        if (text.isEmpty() || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItemsByText(text);
    }

    public Comment createComment(Comment comment, long itemId, long userId) {
        Booking booking = bookingRepository.getBookingByItemIdAndUserId(userId, itemId)
                .orElseThrow(() -> new BookingException("Этот пользователь не может оставить комментарий к этой вещи"));
        if (!(LocalDateTime.now().isAfter(booking.getStart()))) {
            throw new BookingException("Бронирование еще не закончилось");
        }
        comment.setCreatedTime(LocalDateTime.now());
        comment.setAuthorName(booking.getBooker().getName());
        comment.setItem(booking.getItem());
        return commentRepository.save(comment);
    }
}
