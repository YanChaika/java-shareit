package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable Long itemId) {
        log.info("Get item {}, userId={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Create item {}, userId={}", itemDto, userId);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Valid @RequestBody ItemUpdateDto itemDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long itemId) {
        log.info("Update item {}, userId={}, itemId={}", itemDto, userId, itemId);
        return itemClient.updateItem(itemDto, userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get items {}", userId);
        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemByText(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(required = false) String text/*,
                                                @RequestParam(required = false) Long from,
                                                @RequestParam(required = false) Long size*/) {
        //log.info("Get items by text {}, userId={} from={}, size={}", text, userId, from, size);
        //return itemClient.getItemByText(text, userId, from, size);
        log.info("Get items by text {}, userId={}", text, userId);
        return itemClient.getItemByText(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable Long itemId,
                                                @Valid @RequestBody CommentDto comment) {
        log.info("Create comment {}, userId={}, itemId={}", comment, userId, itemId);
        return itemClient.createComment(userId, itemId, comment);
    }

}
