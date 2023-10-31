package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {
    private int id;
    private int owner;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    @JsonProperty(required = true)
    private Boolean available;
    private ItemRequest request;
}
