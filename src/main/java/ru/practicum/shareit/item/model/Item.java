package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Item {
    private int id;
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    private boolean available;
    private final int owner;
    private ItemRequest request;


}
