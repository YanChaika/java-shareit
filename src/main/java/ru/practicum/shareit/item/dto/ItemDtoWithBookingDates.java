package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoWithBookingDates {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @AssertTrue
    private boolean available;
    private Long owner;
    private Integer request;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<Comment> comments;
}
