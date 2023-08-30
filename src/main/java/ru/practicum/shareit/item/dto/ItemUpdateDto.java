package ru.practicum.shareit.item.dto;

import lombok.Data;


@Data
public class ItemUpdateDto {
    private int id;
    private String name;
    private String description;
    private String available;
    private int owner;
    private Integer request;

}

