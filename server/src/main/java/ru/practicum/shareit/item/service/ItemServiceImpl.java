package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.dao.DbItemStorage;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dao.DbUserStorage;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper = new ItemMapper();
    private final CommentMapper commentMapper = new CommentMapper();
    private final BookingMapper bookingMapper = new BookingMapper();
    private final DbItemStorage dbItemStorage;
    private final DbUserStorage dbUserStorage;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto getItemById(long itemId, Integer userId) {
        Item item = dbItemStorage.getItemById(itemId)
                .orElseThrow(() -> new UnknownIdException("Вещи таким id=" + itemId + " не найдено"));
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
            return itemMapper.toItemDtoWithBookingsAndCommentOnlyLastBooking(item, bookingMapper.toBookingDto(lastBooking),
                    commentDtoList);
        }
        ItemDto itemDto = itemMapper.toItemDto(item);
        itemDto.setComments(commentDtoList);
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItems(int userId, Integer from, Integer size) {
        List<ItemDto> itemsWithBookings = new ArrayList<>();
        Pageable pageWithFromAndSize = PageRequest.of(from, size);
        List<ItemDto> itemDtoList =
                itemRepository.findAllBy(pageWithFromAndSize)
                        .stream()
                        .filter(item -> item.getOwner().getId() == userId)
                        .map(itemMapper::toItemDto)
                        .sorted(Comparator.comparing(ItemDto::getId))
                        .collect(Collectors.toList());
        for (ItemDto itemDto : itemDtoList) {
            ItemDto itemWithBookings = getItemById(itemDto.getId(), userId);
            itemsWithBookings.add(itemWithBookings);
        }
        return itemsWithBookings;
    }

    @Override
    public List<ItemDto> searchItemByText(String text, Integer userId, Integer from, Integer size) {
        if (text.isEmpty() || text.isBlank()) {
            return Collections.emptyList();
        }
        Pageable pageWithFromAndSize = PageRequest.of(from, size);
        List<Item> itemList = dbItemStorage.searchItemByText(text, pageWithFromAndSize);
        return itemList.stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(ItemDto itemDto, Integer userId) {
        User user = dbUserStorage.getUserById(userId)
                .orElseThrow(() -> new UnknownIdException("Пользователя таким id=" + userId + " не найдено"));
        itemDto.setOwner(user.getId());
        return dbItemStorage.createItem(itemMapper.toItem(itemDto));
    }

    @Override
    public Item updateItem(ItemDto itemDto, long itemId, Integer userId) {
        return dbItemStorage.getItemById(itemId)
                .filter(item1 -> item1.getOwner().getId() == userId)
                .map(item1 -> {
                    if (itemDto.getAvailable() != null) {
                        item1.setAvailable(itemDto.getAvailable());
                    }
                    if (itemDto.getDescription() != null) {
                        item1.setDescription(itemDto.getDescription());
                    }
                    if (itemDto.getName() != null) {
                        item1.setName(itemDto.getName());
                    }
                    return dbItemStorage.updateItem(item1);
                }).orElseThrow(() -> new UnknownIdException("Вещь не найдена"));
    }

    @Override
    public Comment createComment(CommentDto commentDto, long itemId, Integer userId) {
        Booking booking = bookingRepository.getBookingByItemIdAndUserId(userId, itemId)
                .orElseThrow(() -> new BookingException("Этот пользователь не может оставить комментарий к этой вещи"));
        if (!(LocalDateTime.now().isAfter(booking.getStart()))) {
            throw new BookingException("Бронирование еще не закончилось");
        }
        commentDto.setCreated(LocalDateTime.now());
        commentDto.setAuthorName(booking.getBooker().getName());
        commentDto.setItem(booking.getItem());
        return dbItemStorage.createComment(commentMapper.toComment(commentDto));
    }
}
