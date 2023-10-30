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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Item createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestBody ItemDto itemDto, @PathVariable int itemId, @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId, @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam(value = "text") String text, @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.searchItemByText(text, userId);
    }
}
