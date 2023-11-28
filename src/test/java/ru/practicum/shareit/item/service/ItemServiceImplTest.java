package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@SpringBootTest
@Sql(scripts = {"classpath:schemaTest.sql"},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:dataTest.sql"},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ItemServiceImplTest {

    @Autowired
    ItemService itemService;

    @Test
    void createItemSaveItemInDataBase() {
        Item item = Item.builder()
                .id(4)
                .name("Item4")
                .description("Item4Desc")
                .owner(User.builder()
                        .id(1)
                        .build())
                .available(true)
                .build();
        Item actual = itemService.createItem(ItemDto.builder()
                .name("Item4")
                .description("Item4Desc")
                .available(true)
                .build(), 1);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(item, actual);
    }

    @Test
    void createItemThrowExceptionIfUserIdNotFound() {
        ItemDto itemDto = ItemDto.builder()
                .name("Item3")
                .description("Item3Desc")
                .available(true)
                .build();
        UnknownIdException exception = Assertions.assertThrows(UnknownIdException.class,
                () -> itemService.createItem(itemDto, 99));
        Assertions.assertEquals("Пользователя таким id=99 не найдено", exception.getMessage());
    }

    @Test
    void getItemByIdReturnValidItemIfValidId() {
        List<CommentDto> commentDtos = new ArrayList<>();
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("Комментарий")
                .authorName("Автор")
                .created(LocalDateTime.of(2023, 11, 24, 15, 35, 30))
                .build();
        commentDtos.add(commentDto);
        ItemDto expected = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("Item1Desc")
                .owner(1)
                .available(true)
                .lastBooking(BookingDto.builder()
                        .id(1)
                        .bookerId(1)
                        .itemId(0)
                        .build())
                .nextBooking(BookingDto.builder()
                        .id(2)
                        .bookerId(2)
                        .itemId(0)
                        .build())
                .comments(commentDtos)
                .requestId(0)
                .build();
        ItemDto actual = itemService.getItemById(1, 1);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getItemByIdReturnValidItemIWithOnlyLastBookingIfValidId() {
        ItemDto expected = ItemDto.builder()
                .id(3)
                .name("Item3")
                .description("Item3Desc")
                .owner(2)
                .available(true)
                .lastBooking(BookingDto.builder()
                        .id(4)
                        .bookerId(2)
                        .itemId(0)
                        .build())
                .comments(Collections.emptyList())
                .requestId(0)
                .build();
        ItemDto actual = itemService.getItemById(3, 2);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getItemByIdReturnValidItemNoBookingsIfValidId() {
        ItemDto expected = ItemDto.builder()
                .id(3)
                .name("Item3")
                .description("Item3Desc")
                .owner(2)
                .available(true)
                .lastBooking(BookingDto.builder()
                        .id(4)
                        .bookerId(2)
                        .itemId(0)
                        .build())
                .requestId(0)
                .comments(Collections.emptyList())
                .build();
        ItemDto actual = itemService.getItemById(3, 2);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getItemByIdThrowExceptionIfItemIdNotFound() {
        UnknownIdException exception = Assertions.assertThrows(UnknownIdException.class,
                () -> itemService.getItemById(99, 1));
        Assertions.assertEquals("Вещи таким id=99 не найдено", exception.getMessage());
    }

    @Test
    void getAllItemsReturnValidItemsIfItemsTableNotEmpty() {
        List<CommentDto> commentDtos = new ArrayList<>();
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("Комментарий")
                .authorName("Автор")
                .created(LocalDateTime.of(2023, 11, 24, 15, 35, 30))
                .build();
        commentDtos.add(commentDto);
        List<ItemDto> expected = new ArrayList<>();
        ItemDto itemDto1 = ItemDto.builder()
                .id(1)
                .name("Item1")
                .description("Item1Desc")
                .owner(1)
                .available(true)
                .lastBooking(BookingDto.builder()
                        .id(1)
                        .bookerId(1)
                        .itemId(0)
                        .build())
                .nextBooking(BookingDto.builder()
                        .id(2)
                        .bookerId(2)
                        .itemId(0)
                        .build())
                .comments(commentDtos)
                .requestId(0)
                .build();
        expected.add(itemDto1);
        Assertions.assertEquals(expected, itemService.getAllItems(1, 0, 2));
    }

    @Test
    void updateItemReturnValidItemIfValidArgument() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Item1Upd")
                .description("Item1DescUpd")
                .available(true)
                .build();

        Item expected = Item.builder()
                .id(1)
                .owner(User.builder()
                        .id(1)
                        .name("User1")
                        .email("User1@mail.ru")
                        .build())
                .name("Item1Upd")
                .description("Item1DescUpd")
                .available(true)
                .requestId(0)
                .build();
        Item itemUpd = itemService.updateItem(itemDto, 1, 1);
        Assertions.assertNotNull(itemUpd);
        Assertions.assertEquals(expected, itemUpd);
    }

    @Test
    void updateItemThrowExceptionIfItemIdNotFound() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Item1Upd")
                .description("Item1DescUpd")
                .build();
        UnknownIdException exception = Assertions.assertThrows(UnknownIdException.class,
                () -> itemService.updateItem(itemDto, 99, 1));
        Assertions.assertEquals("Вещь не найдена", exception.getMessage());
    }

    @Test
    void searchItemByTextReturnValidListIfValidArguments() {
        List<ItemDto> actual = itemService.searchItemByText("Desc", 1, 0, 2);
        List<ItemDto> expected = new ArrayList<>();
        ItemDto itemDto1 = ItemDto.builder()
                .id(1)
                .owner(1)
                .name("Item1")
                .description("Item1Desc")
                .available(true)
                .requestId(0)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .id(3)
                .owner(2)
                .name("Item3")
                .description("Item3Desc")
                .available(true)
                .requestId(0)
                .build();
        expected.add(itemDto1);
        expected.add(itemDto2);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void searchItemByTextReturnEmptyListIfTextIsBlank() {
        List<ItemDto> actual = itemService.searchItemByText("", 1, 0, 2);
        List<ItemDto> expected = Collections.emptyList();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createCommentReturnValidCommentIfValidArgument() {
        CommentDto commentDto = CommentDto.builder()
                .text("Комментарий")
                .build();
        Comment expected = Comment.builder()
                .id(2)
                .item(Item.builder()
                        .id(2)
                        .owner(User.builder()
                                .id(2)
                                .name("User2")
                                .email("User2@mail.ru")
                                .build())
                        .name("Item2")
                        .description("Item2Desc")
                        .available(false)
                        .requestId(1)
                        .build())
                .createdTime(commentDto.getCreated())
                .authorName("User2")
                .text("Комментарий")
                .build();
        Comment actual = itemService.createComment(commentDto, 2, 2);
        expected.setCreatedTime(actual.getCreatedTime());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createCommentThrowExceptionIfNotValidUserId() {
        CommentDto commentDto = CommentDto.builder()
                .text("Комментарий")
                .build();
        BookingException exception = Assertions.assertThrows(BookingException.class,
                () -> itemService.createComment(commentDto, 1, 1));
        Assertions.assertEquals("Этот пользователь не может оставить комментарий к этой вещи", exception.getMessage());
    }

    @Test
    void createCommentThrowExceptionIfBookingNotEnd() {
        CommentDto commentDto = CommentDto.builder()
                .text("Комментарий")
                .build();
        BookingException exception = Assertions.assertThrows(BookingException.class,
                () -> itemService.createComment(commentDto, 1, 2));
        Assertions.assertEquals("Бронирование еще не закончилось", exception.getMessage());
    }
}
