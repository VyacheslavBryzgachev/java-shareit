package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dao.DbRequestStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.DbUserStorage;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {

    private final DbUserStorage dbUserStorage;
    private final DbRequestStorage dbRequestStorage;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper = new ItemRequestMapper();

    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto, long userId) {
        User user = dbUserStorage.getUserById(userId)
                .orElseThrow(() -> new UnknownIdException("Пользователя таким id=" + userId + " не найдено"));
        itemRequestDto.setRequester(user.getId());
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        return itemRequestMapper.toItemRequestDto(dbRequestStorage.create(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getAll(long userId) {
        dbUserStorage.getUserById(userId)
                .orElseThrow(() -> new UnknownIdException("Пользователя с таким id не найдено"));
        List<ItemRequestDto> requests = dbRequestStorage.getAllUserRequests(userId)
                .stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        List<Long> requestIds = requests.stream()
                .map(ItemRequestDto::getId)
                .collect(Collectors.toList());
        List<Item> allItems = itemRepository.getItemsByRequestIds(requestIds);

        for (ItemRequestDto request : requests) {
            List<Item> items = allItems.stream()
                    .filter(item -> item.getRequestId() == request.getId())
                    .collect(Collectors.toList());
            request.setItems(items);
        }
        return requests;
    }

    @Override
    public ItemRequestDto getById(long userId, long requestId) {
        dbUserStorage.getUserById(userId)
                .orElseThrow(() -> new UnknownIdException("Пользователя с таким id не найдено"));
        ItemRequest itemRequest =
                dbRequestStorage.getById(requestId).orElseThrow(() -> new UnknownIdException("Запроса с таким Id не найдено"));
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        List<Item> items = itemRepository.getItemsByRequestId(itemRequestDto.getId());
        itemRequestDto.setItems(items);
        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getAllFromAnotherUser(long userId, Integer from, Integer size) {
        Pageable pageWithFromAndSize = PageRequest.of(from, size);
        Page<ItemRequest> requests = dbRequestStorage.getAllFromAnotherUser(pageWithFromAndSize);
        List<ItemRequestDto> requestsDto = requests
                .stream()
                .filter(itemRequest -> itemRequest.getRequester().getId() != userId)
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        List<Long> requestIds = requestsDto.stream()
                .map(ItemRequestDto::getId)
                .collect(Collectors.toList());
        List<Item> allItems = itemRepository.getItemsByRequestIds(requestIds);
        for (ItemRequestDto requestDto : requestsDto) {
            List<Item> items = allItems.stream()
                    .filter(item -> item.getRequestId() == requestDto.getId())
                    .collect(Collectors.toList());
            requestDto.setItems(items);
        }
        return requestsDto;
    }
}
