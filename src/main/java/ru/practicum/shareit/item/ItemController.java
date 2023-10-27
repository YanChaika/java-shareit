package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateUser(@RequestBody ItemUpdateDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId) {
        return itemService.update(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingDates getItemBuId(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long itemId) {
        return itemService.findItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoWithBookingDates> getAllItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                            @RequestParam(required = false) Long from,
                                                            @RequestParam(required = false) Long size) {
        return itemService.getAllByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByText(@RequestParam(required = false) String text,
                                       @RequestParam(required = false) Long from,
                                       @RequestParam(required = false) Long size) {
        return itemService.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentFullDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long itemId,
                                        @Valid @RequestBody CommentDto comment) {
        if (comment.getText().isBlank()) {
            throw new ValidationException("text is empty");
        }
        return itemService.createComment(comment, itemId, userId);
    }
}
