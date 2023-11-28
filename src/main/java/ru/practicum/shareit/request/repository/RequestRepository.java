package ru.practicum.shareit.request.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query(value = "SELECT * FROM Requests WHERE requester = :userId", nativeQuery = true)
    List<ItemRequest> getAllUserRequests(@Param("userId") long userId);

    Page<ItemRequest> findAllBy(Pageable pageable);
}
