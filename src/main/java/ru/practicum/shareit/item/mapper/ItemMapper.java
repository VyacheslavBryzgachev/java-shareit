package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getOwner(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null);
    }

    public Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(),
                itemDto.getOwner(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequest() != null ? itemDto.getRequest() : null);
    }
}
