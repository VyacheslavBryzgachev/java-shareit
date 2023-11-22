package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {
    ItemRequest create(ItemRequestDto itemRequestDto, long userId);

    List<ItemRequestDto> getAll(long userId);

    ItemRequestDto getById(long userId, long requestId);

    List<ItemRequestDto> getAllFromAnotherUser(long userId, Integer from, Integer size);

}
