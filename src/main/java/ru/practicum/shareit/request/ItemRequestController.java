package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFullDto;
import ru.practicum.shareit.request.dto.ItemRequestWithTimeDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final RequestService requestService;

    @PostMapping
    public ItemRequestWithTimeDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return requestService.create(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestFullDto>> findAllMyRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(requestService.findAllMyRequests(userId));
    }

    @GetMapping("/all")
    public List<ItemRequestFullDto> findAllRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(required = false) Long from,
                                                @RequestParam(required = false) Long size) {
        
        return requestService.findAllRequestsByOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestFullDto findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long requestId) {
        return requestService.findById(userId, requestId);
    }
}
