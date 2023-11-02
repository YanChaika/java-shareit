package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void getAllByUserId_whenUserFound_thenReturnUsers() {
        long userId = 0L;
        Long from = 1L;
        Long size = 1L;
        long itemId = 1L;
        Item item = new Item(
                itemId,
                "name",
                "description",
                true,
                0L,
                new ItemRequest()
        );
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now();
        Booking booking = new Booking(
                1L,
                startTime,
                endTime,
                item,
                new User(),
                BookingStatus.WAITING
        );
        List<CommentFullDto> comments = new ArrayList<>();
        ItemDtoWithBookingDates itemDtoWithBookingDates = ItemMapper
                .toItemDtoWithBookingDates(
                        item,
                        BookingMapper.toBookingToItemDto(booking),
                        null,
                        comments
                );
        when(itemRepository.findAll(PageRequest.of(from.intValue(), size.intValue())))
                .thenReturn(new PageImpl<>(List.of(item)));
        when(bookingRepository.findAllByItemIdOrderByStartDesc(item.getId()))
                .thenReturn(List.of(booking));
        when(commentRepository.findAll()).thenReturn(List.of(new Comment()));

        List<ItemDtoWithBookingDates> actualItemRequest = itemService.getAllByUserId(userId, from, size);

        assertEquals(List.of(itemDtoWithBookingDates), actualItemRequest);
    }

    @Test
    void getAllByUserId_whenFromAndSizeZero_thenReturnUsers() {
        long userId = 0L;
        Long from = 0L;
        Long size = 0L;

        assertThrows(ValidationException.class, () -> itemService.getAllByUserId(userId, from, size));
    }

    @Test
    void create_whenInvoked_thenReturnedItem() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item();
        item.setId(null);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("itemDto");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ItemDto actualItem = itemService.create(itemDto, userId);

        assertEquals(itemDto, actualItem);
    }

    @Test
    void create_whenNotInvoked_thenReturnedItem() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item();
        item.setId(null);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("itemDto");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(1L);
        itemDto.setRequestId(itemRequest.getId());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.create(itemDto, userId));
    }

    @Test
    void create_whenInvokedAndGetRequest_thenReturnedItem() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item();
        item.setId(null);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("itemDto");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(1L);
        itemDto.setRequestId(itemRequest.getId());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findById(itemDto.getRequestId())).thenReturn(Optional.of(itemRequest));

        ItemDto actualItem = itemService.create(itemDto, userId);

        assertEquals(itemDto, actualItem);
    }

    @Test
    void update_whenInvoked_thenReturnedItem() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
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
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("item");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.saveAndFlush(any(Item.class))).thenReturn(item);

        ItemDto actualItem = itemService.update(itemUpdateDto, userId, item.getId());

        assertEquals(itemDto, actualItem);
    }

    @Test
    void update_whenNotInvoked_thenReturnedException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
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
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("item");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.update(itemUpdateDto, userId, item.getId()));
    }

    @Test
    void update_whenNotAvailableInvoked_thenReturnedItem() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(false);
        item.setOwnerId(1L);
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);
        itemDto.setOwner(1L);
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("item");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("false");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.saveAndFlush(any(Item.class))).thenReturn(item);

        ItemDto actualItem = itemService.update(itemUpdateDto, userId, item.getId());

        assertEquals(itemDto, actualItem);
    }

    @Test
    void update_whenAvailableNull_thenReturnedException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(false);
        item.setOwnerId(1L);
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);
        itemDto.setOwner(1L);
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("item");
        itemUpdateDto.setDescription("description");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.saveAndFlush(any(Item.class))).thenReturn(item);

        ItemDto actualItem = itemService.update(itemUpdateDto, userId, item.getId());

        assertEquals(itemDto, actualItem);
    }

    @Test
    void findItemById_whenInvoked_thenReturnedItem() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
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
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemDto");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdOrderByStartDesc(item.getId())).thenReturn(List.of(booking));
        when(commentRepository.findAll()).thenReturn(List.of(comment));
        when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of(comment));

        ItemDtoWithBookingDates actualItem = itemService.findItemById(item.getId(), userId);

        assertEquals(itemDto.getName(), actualItem.getName());
    }

    @Test
    void findItemById_whenInvokedOwner_thenReturnedItem() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
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
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemDto");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdOrderByStartDesc(item.getId())).thenReturn(List.of(booking));
        when(commentRepository.findAll()).thenReturn(List.of(comment));
        when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of(comment));

        ItemDtoWithBookingDates actualItem = itemService.findItemById(item.getId(), userId);

        assertEquals(itemDto.getName(), actualItem.getName());
    }

    @Test
    void findItemById_whenInvoked_thenReturnedItemWithLastBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(user.getId());
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemDto");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        Booking bookingLast = new Booking(
                2L,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        Booking bookingNext = new Booking(
                2L,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(3),
                item,
                user,
                BookingStatus.APPROVED);
        Booking booking3 = new Booking(
                3L,
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(7),
                item,
                user,
                BookingStatus.APPROVED);
        Booking booking4 = new Booking(
                4L,
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2),
                item,
                user,
                BookingStatus.APPROVED);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdOrderByStartDesc(item.getId()))
                .thenReturn(List.of(booking, bookingLast, bookingNext, booking4, booking3));
        when(commentRepository.findAll()).thenReturn(List.of(comment));
        when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of(comment));

        ItemDtoWithBookingDates actualItem = itemService.findItemById(item.getId(), userId);

        assertEquals(itemDto.getName(), actualItem.getName());
    }

    @Test
    void findItemById_whenInvokedLastBookingIsEmpty_thenReturnedItemWithLastBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(user.getId());
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemDto");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                user,
                BookingStatus.APPROVED);
        Booking bookingNext = new Booking(
                2L,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(3),
                item,
                user,
                BookingStatus.APPROVED);
        Booking booking3 = new Booking(
                3L,
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(7),
                item,
                user,
                BookingStatus.APPROVED);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdOrderByStartDesc(item.getId()))
                .thenReturn(List.of(booking, bookingNext, booking3));
        when(commentRepository.findAll()).thenReturn(List.of(comment));
        when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of(comment));

        ItemDtoWithBookingDates actualItem = itemService.findItemById(item.getId(), userId);

        assertEquals(itemDto.getName(), actualItem.getName());
    }

    @Test
    void findItemById_whenInvokedNextBooking_thenReturnedItemWithLastBooking() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
        User user = new User(userId, "name", "emsil@emsil.com");
        ItemRequest itemRequest = new ItemRequest(1L, "description", user.getId(), LocalDateTime.now());
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(user.getId());
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemDto");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                item,
                user,
                BookingStatus.APPROVED);
        Booking bookingLast = new Booking(
                2L,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        Booking booking4 = new Booking(
                4L,
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2),
                item,
                user,
                BookingStatus.APPROVED);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdOrderByStartDesc(item.getId()))
                .thenReturn(List.of(booking, bookingLast, booking4));
        when(commentRepository.findAll()).thenReturn(List.of(comment));
        when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of(comment));

        ItemDtoWithBookingDates actualItem = itemService.findItemById(item.getId(), userId);

        assertEquals(itemDto.getName(), actualItem.getName());
    }

    @Test
    void findItemById_whenItemNotFound_thenReturnedException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
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
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemDto");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.findItemById(item.getId(), userId));
    }

    @Test
    void getAll_whenInvoked_thenReturnedItem() {
        long userId = 1L;
        Long from = 0L;
        Long size = 0L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
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
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<ItemDto> actualItem = itemService.getAll();

        assertEquals(List.of(itemDto), actualItem);
    }

    @Test
    void search_whenInvoked_thenReturnedItem() {
        long userId = 1L;
        Long from = 0L;
        Long size = 1L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
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
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemDto");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        String query = "i";
        when(itemRepository.findItemsByQuery(query)).thenReturn(List.of(item));

        List<ItemDto> actualItem = itemService.search(query, from, size);

        assertEquals(List.of(itemDto), actualItem);
    }

    @Test
    void createComment_whenInvoked_thenReturnedItem() {
        long userId = 1L;
        Long from = 0L;
        Long size = 1L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
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
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemDto");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment.setText("text");
        CommentDto commentDto = new CommentDto();
        commentDto.setText("text");
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(userId)).thenReturn(List.of(booking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentFullDto actualComment = itemService.createComment(commentDto, item.getId(), userId);

        assertEquals(commentDto.getText(), actualComment.getText());
    }

    @Test
    void createComment_whenNotFoundBooking_thenReturnedException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 1L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
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
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemDto");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                user,
                BookingStatus.APPROVED);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment.setText("text");
        CommentDto commentDto = new CommentDto();
        commentDto.setText("text");
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(userId)).thenReturn(new ArrayList<>());

        assertThrows(NotFoundException.class, () -> itemService.createComment(commentDto, item.getId(), userId));
    }

    @Test
    void createComment_whenNotInvoked_thenReturnedException() {
        long userId = 1L;
        Long from = 0L;
        Long size = 1L;
        LocalDateTime timeStart = LocalDateTime.now();
        LocalDateTime timeEnd = LocalDateTime.now().plusDays(1L);
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
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemDto");
        itemUpdateDto.setDescription("description");
        itemUpdateDto.setAvailable("true");
        Booking booking = new Booking(
                1L,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                user,
                BookingStatus.REJECTED);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, user, item);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment.setText("text");
        CommentDto commentDto = new CommentDto();
        commentDto.setText("text");
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(userId)).thenReturn(List.of(booking));

        assertThrows(ValidationException.class, () -> itemService.createComment(commentDto, item.getId(), userId));
    }


}