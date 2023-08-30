package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDto {
    private int id;
    @NotBlank
    private final String name;
    @NotBlank
    private final String email;
}
