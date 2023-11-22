package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper = new ItemMapper();
    private final CommentMapper commentMapper = new CommentMapper();

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemMapper.toItemDto(itemService.createItem(itemDto, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable long itemId,
                              @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemMapper.toItemDto(itemService.updateItem(itemDto, itemId, userId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId, @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                     @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                     @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return itemService.getAllItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam(value = "text") String text,
                                          @RequestHeader(value = "X-Sharer-User-Id") int userId,
                                          @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                          @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return itemService.searchItemByText(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto, @PathVariable long itemId,
                                    @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return commentMapper.toCommentDto(itemService.createComment(commentDto, itemId, userId));
    }
}
