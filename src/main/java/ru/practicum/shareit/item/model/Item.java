package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item {
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
    private String request;
}
