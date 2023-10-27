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
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDates;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

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

}