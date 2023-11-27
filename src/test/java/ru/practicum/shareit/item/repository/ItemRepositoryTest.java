package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@DataJpaTest
@Transactional
@Sql(scripts = {"classpath:schemaTest.sql"},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:dataTest.sql"},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void searchItemsByTextTest() {
        List<Item> items = itemRepository.searchItemsByText("desc",PageRequest.of(0, 10));
        Assertions.assertEquals(2, items.size());
    }

    @Test
    void getItemsByRequestIdTest() {
        List<Item> items = itemRepository.getItemsByRequestId(1);
        Assertions.assertEquals(1, items.size());
    }

}
