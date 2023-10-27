package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestFullDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

}
