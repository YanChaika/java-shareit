package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFullDto;
import ru.practicum.shareit.request.dto.ItemRequestWithTimeDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    void findAllRequestsByOtherUsers_whenItemRequestsFound_returnItemRequests() {
        long ownerId = 0L;
        Long from = 0L;
        Long size = 1L;
        LocalDateTime time = LocalDateTime.now();
        ItemRequest itemRequest = new ItemRequest(ownerId, "description", 2L, time);
        ItemRequestFullDto itemRequestFullDtoToCheck = RequestMapper
                .toItemRequestFullDto(itemRequest, List.of(new ItemDto()));
        when(requestRepository.findAll(PageRequest.of(from.intValue(), size.intValue())))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));
        when(itemRepository.findAllByRequestIdOrderByRequestCreatedDesc(itemRequest.getId()))
                .thenReturn(List.of(new Item()));
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(new User()));

        List<ItemRequestFullDto> actualItemRequestsFullDto = requestService
                .findAllRequestsByOtherUsers(ownerId, from, size);

        assertEquals(List.of(itemRequestFullDtoToCheck), actualItemRequestsFullDto);
    }

    @Test
    void create_whenInvoked_thenReturnedItemRequest() {
        long userId = 1L;
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(1L);
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(1L);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestWithTimeDto actualItem = requestService.create(itemRequestDto, userId);

        assertEquals(itemRequestDto.getDescription(), actualItem.getDescription());
    }

    @Test
    void findAllMyRequests_whenInvoked_thenReturnedItemRequest() {
        long userId = 1L;
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(1L);
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(1L);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequesterIdOrderByCreatedDesc(userId)).thenReturn(List.of(itemRequest));

        List<ItemRequestFullDto> actualItem = requestService.findAllMyRequests(userId);

        assertEquals(1, actualItem.size());
    }

    @Test
    void findBuId_whenInvoked_thenReturnedItemRequest() {
        long userId = 1L;
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(1L);
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(1L);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestIdOrderByRequestCreatedDesc(itemRequest.getId())).thenReturn(List.of(item));

        ItemRequestFullDto actualItem = requestService.findById(userId, itemRequest.getId());

        assertEquals(itemRequest.getDescription(), actualItem.getDescription());
    }
}
