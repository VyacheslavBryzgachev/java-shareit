package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class DbItemStorage {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper = new ItemMapper();
    private final CommentMapper commentMapper = new CommentMapper();
    private final BookingMapper bookingMapper = new BookingMapper();

    public ItemDto getItemById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new UnknownIdException("Вещи таким id=" + itemId + " не найдено"));
        List<Comment> comments = commentRepository.getCommentByItemId(itemId);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtoList.add(commentMapper.toCommentDto(comment));
        }
        Booking lastBooking = bookingRepository.getLastBooking(itemId);
        Booking nextBooking = bookingRepository.getNextBooking(itemId);
        if (!(lastBooking == null) && !(nextBooking == null) && item.getOwner().getId() == userId) {
            return itemMapper.toItemDtoWithBookingsAndComment(item, bookingMapper.toBookingDto(lastBooking),
                    bookingMapper.toBookingDto(nextBooking), commentDtoList);
        } else if (!(lastBooking == null) && nextBooking == null && item.getOwner().getId() == userId) {
            return itemMapper.toItemDtoWithBookingsAndCommentOnlyLastBooking(item, bookingMapper.toBookingDto(lastBooking), commentDtoList);
        }
        ItemDto itemDto = itemMapper.toItemDto(item);
        itemDto.setComments(commentDtoList);
        return itemDto;
    }

    public List<ItemDto> gelAllItems(int userId) {
        List<ItemDto> itemsWithBookings = new ArrayList<>();
        List<ItemDto> itemDtoList =
                itemRepository.findAll().stream().filter(item -> item.getOwner().getId() == userId).map(itemMapper::toItemDto)
                        .sorted(Comparator.comparing(ItemDto::getId)).collect(Collectors.toList());
        for (ItemDto itemDto : itemDtoList) {
            ItemDto itemWithBookings = getItemById(itemDto.getId(), userId);
            itemsWithBookings.add(itemWithBookings);
        }
        return itemsWithBookings;
    }

    public ItemDto createItem(Item item, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnknownIdException("Пользователя таким id=" + userId + " не найдено"));
        item.setOwner(user);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    public ItemDto updateItem(Item item, long itemId, int userId) {
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
            return itemMapper.toItemDto(itemRepository.save(item1));
        }).orElseThrow(() -> new UnknownIdException("Вещь не найдена"));
    }

    public List<Item> searchItemByText(String text, int userId) {
        if (text.isEmpty() || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItemsByText(text);
    }

    public Comment createComment(CommentDto commentDto, long itemId, long userId) {
        Booking booking = bookingRepository.getBookingByItemIdAndUserId(userId, itemId)
                .orElseThrow(() -> new BookingException("Этот пользователь не может оставить комментарий к этой вещи"));
        if (!(LocalDateTime.now().isAfter(booking.getStart()))) {
            throw new BookingException("Бронирование еще не закончилось");
        }
        commentDto.setCreated(LocalDateTime.now());
        commentDto.setAuthorName(booking.getBooker().getName());
        commentDto.setItem(booking.getItem());
        return commentRepository.save(commentMapper.toComment(commentDto));
    }
}
