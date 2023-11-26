package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
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
public class RequestServiceImplTest {

    @Autowired
    RequestService requestService;

    @Test
    void createReturnValidRequestIfValidArguments() {
        List<Item> items = new ArrayList<>();
        Item item = Item
                .builder()
                .id(1)
                .name("Item1")
                .owner(User
                        .builder()
                        .id(1)
                        .name("User1")
                        .email("User1@mail.ru")
                        .build())
                .build();
        items.add(item);
        ItemRequestDto itemRequest = ItemRequestDto
                .builder()
                .id(2)
                .items(items)
                .created(LocalDateTime.now())
                .description("Описание")
                .build();
        ItemRequest expected = ItemRequest
                .builder()
                .id(2)
                .requester(User
                        .builder()
                        .id(1)
                        .name("User1")
                        .email("User1@mail.ru")
                        .build())
                .description("Описание")
                .build();
        ItemRequest actual = requestService.create(itemRequest, 1);
        expected.setCreatedTime(actual.getCreatedTime());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllReturnValidListIfValidUserId() {
        List<Item> items = new ArrayList<>();
        Item item = Item
                .builder()
                .id(2)
                .name("Item2")
                .description("Item2Desc")
                .available(false)
                .requestId(1)
                .owner(User
                        .builder()
                        .id(2)
                        .name("User2")
                        .email("User2@mail.ru")
                        .build())
                .build();
        items.add(item);
        List<ItemRequestDto> expected = new ArrayList<>();
        ItemRequestDto itemRequestDto = ItemRequestDto
                .builder()
                .id(1)
                .requester(1)
                .description("Описание1")
                .items(items)
                .build();
        List<ItemRequestDto> actual = requestService.getAll(1);
        itemRequestDto.setCreated(actual.get(0).getCreated());
        expected.add(itemRequestDto);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    void getByIdReturnValidRequestIfValidArguments() {
        List<Item> items = new ArrayList<>();
        Item item = Item
                .builder()
                .id(2)
                .name("Item2")
                .description("Item2Desc")
                .available(false)
                .requestId(1)
                .owner(User
                        .builder()
                        .id(2)
                        .name("User2")
                        .email("User2@mail.ru")
                        .build())
                .build();
        items.add(item);
        ItemRequestDto expected = ItemRequestDto
                .builder()
                .id(1)
                .requester(1)
                .description("Описание1")
                .items(items)
                .build();
        ItemRequestDto actual = requestService.getById(1, 1);
        expected.setCreated(actual.getCreated());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAllFromAnotherUserReturnValidListIfValidArguments() {
        List<ItemRequestDto> expected = new ArrayList<>();
        ItemRequestDto itemRequestDto = ItemRequestDto
                .builder()
                .id(2)
                .requester(2)
                .description("Описание2")
                .items(Collections.emptyList())
                .build();
        List<ItemRequestDto> actual = requestService.getAllFromAnotherUser(1, 0, 2);
        itemRequestDto.setCreated(actual.get(0).getCreated());
        expected.add(itemRequestDto);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected.get(0), actual.get(0));
    }
}
