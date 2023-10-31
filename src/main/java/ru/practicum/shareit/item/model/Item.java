package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private int id;
    private int owner;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
}
