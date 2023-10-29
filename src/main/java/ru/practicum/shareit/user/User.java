package ru.practicum.shareit.user;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private int id;
    private String name;
    @Email
    @NotNull
    private String email;
    private List<Item> usersItems = new ArrayList<>();

}
