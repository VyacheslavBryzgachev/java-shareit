package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return requestClient.create(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return requestClient.getAll(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(value = "X-Sharer-User-Id") long userId, @PathVariable Long requestId) {
        return requestClient.getById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllFromAnotherUser(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                        @RequestParam(value = "from", defaultValue = "0", required = false) @Positive Integer from,
                                                        @RequestParam(value = "size", defaultValue = "10", required = false) @Positive Integer size) {
        return requestClient.getAllFromAnotherUser(userId, from, size);
    }
}
