package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "SELECT i FROM Item i \n" +
            "WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :text ,'%'))\n" +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%' , :text, '%'))) AND i.available = true")
    List<Item> searchItemsByText(@Param("text") String text, Pageable pageable);

    List<Item> getItemsByRequestId(long requestId);

    Page<Item> findAllBy(Pageable pageable);
}
