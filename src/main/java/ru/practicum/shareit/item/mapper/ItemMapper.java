package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .owner(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

    public Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .owner(User.builder()
                        .id(itemDto.getOwner())
                        .build())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .requestId(itemDto.getRequestId())
                .build();
    }

    public Item toItemWithRequest(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .owner(User.builder()
                        .id(itemDto.getOwner())
                        .build())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .requestId(itemDto.getRequestId())
                .build();
    }

    public ItemDto toItemDtoWithBookingsAndComment(Item item, BookingDto lastBooking, BookingDto nextBooking,
                                                   List<CommentDto> commentsDto) {
        return ItemDto.builder()
                .id(item.getId())
                .owner(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(BookingDto.builder()
                        .id(lastBooking.getId())
                        .bookerId(lastBooking.getBookerId())
                        .build())
                .nextBooking(BookingDto.builder()
                        .id(nextBooking.getId())
                        .bookerId(nextBooking.getBookerId())
                        .build())
                .comments(commentsDto)
                .build();
    }

    public ItemDto toItemDtoWithBookingsAndCommentOnlyLastBooking(Item item, BookingDto lastBooking, List<CommentDto> commentsDto) {
        return ItemDto.builder()
                .id(item.getId())
                .owner(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(BookingDto.builder()
                        .id(lastBooking.getId())
                        .bookerId(lastBooking.getBookerId())
                        .build())
                .comments(commentsDto)
                .build();
    }

    public ItemDto toItemDtoWithBookingsAndCommentOnlyNextBooking(Item item, BookingDto next, List<CommentDto> commentsDto) {
        return ItemDto.builder()
                .id(item.getId())
                .owner(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .nextBooking(BookingDto.builder()
                        .id(next.getId())
                        .bookerId(next.getBookerId())
                        .build())
                .comments(commentsDto)
                .build();
    }
}
