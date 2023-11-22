package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.RequestServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestServiceImpl requestService;
    private final ItemRequestMapper itemRequestMapper = new ItemRequestMapper();

    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemRequestMapper.toItemRequestDto(requestService.create(itemRequestDto, userId));
    }

    @GetMapping
    public List<ItemRequestDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return requestService.getAll(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader(value = "X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        return requestService.getById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllFromAnotherUser(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                      @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                                      @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return requestService.getAllFromAnotherUser(userId, from, size);
    }
}
