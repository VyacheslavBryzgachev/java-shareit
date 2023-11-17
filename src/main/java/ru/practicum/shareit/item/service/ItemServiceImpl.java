package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dao.DbItemStorage;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.ArrayList;
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
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto getItemById(long itemId, int userId) {
        Item item = dbItemStorage.getItemById(itemId, userId);
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
    public List<ItemDto> getAllItems(int userId) {
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

    @Override
    public List<ItemDto> searchItemByText(String text, int userId) {
        List<Item> itemList = dbItemStorage.searchItemByText(text, userId);
        return itemList.stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(ItemDto itemDto, int userId) {
        itemDto.setOwner(userId);
        return dbItemStorage.createItem(itemMapper.toItem(itemDto), userId);
    }

    @Override
    public Item updateItem(ItemDto itemDto, long itemId, int userId) {
        return dbItemStorage.updateItem(itemMapper.toItem(itemDto), itemId, userId);
    }

    @Override
    public Comment createComment(CommentDto commentDto, long itemId, long userId) {
        return dbItemStorage.createComment(commentMapper.toComment(commentDto), itemId, userId);
    }
}
