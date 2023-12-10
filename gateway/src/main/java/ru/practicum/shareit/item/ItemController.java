package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collections;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                                             @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId, @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                              @RequestParam(value = "from", defaultValue = "0", required = false) @Positive Integer from,
                                              @RequestParam(value = "size", defaultValue = "10", required = false) @Positive Integer size) {
        return itemClient.getAllItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestParam(value = "text") String text,
                                                   @RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                   @RequestParam(value = "from", defaultValue = "0", required = false)
                                                   @Positive Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10", required = false)
                                                   @Positive Integer size) {
        if (text.isEmpty() || text.isBlank()) {
            return ResponseEntity.ok(Collections.EMPTY_LIST);
        }
        return itemClient.searchItemByText(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto, @PathVariable Long itemId,
                                                @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemClient.createComment(commentDto, itemId, userId);
    }
}
