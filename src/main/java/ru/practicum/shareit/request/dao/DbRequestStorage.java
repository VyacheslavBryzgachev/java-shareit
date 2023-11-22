package ru.practicum.shareit.request.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class DbRequestStorage {
    private final RequestRepository requestRepository;

    public ItemRequest create(ItemRequest itemRequest) {
        return requestRepository.save(itemRequest);
    }

    public List<ItemRequest> getAllUserRequests(long userId) {
        return requestRepository.getAllUserRequests(userId);
    }

    public Optional<ItemRequest> getById(long requestId) {
        return requestRepository.findById(requestId);
    }

    public Page<ItemRequest> getAllFromAnotherUser(Pageable pageable) {
        return requestRepository.findAllBy(pageable);
    }

    public List<ItemRequest> getAll() {
        return requestRepository.findAll();
    }
}
