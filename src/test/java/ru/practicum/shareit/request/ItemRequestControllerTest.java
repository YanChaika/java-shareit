package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFullDto;
import ru.practicum.shareit.request.dto.ItemRequestWithTimeDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private RequestServiceImpl requestService;

    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemRequestDto itemRequestDto;
    private ItemRequestWithTimeDto itemRequestWithTimeDto;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemRequestDto = new ItemRequestDto(
                1L,
                "Хотел бы воспользоваться щёткой для обуви"
        );
    }

    @Test
    void saveNewItemRequest_whenItemRequestSave_thenReturnItemRequest() throws Exception {
        long userId = 1L;
        LocalDateTime time = LocalDateTime.now();
        itemRequest = RequestMapper.fromItemRequestDto(itemRequestDto, userId, time);
        itemRequestWithTimeDto = RequestMapper.toItemRequestWithTimeDto(itemRequest);
        when(requestService.create(itemRequestDto, userId))
                .thenReturn(itemRequestWithTimeDto);

        mvc.perform(post("/requests")
                    .content(mapper.writeValueAsString(itemRequestDto))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", 1)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }

    @Test
    void getAllMyRequests_whenInvoked_thenResponseStatusOkWithItemRequestCollectionBody() {
        List<ItemRequestFullDto> expectedItemRequests = List.of(new ItemRequestFullDto());
        when(requestService.findAllMyRequests(anyLong())).thenReturn(expectedItemRequests);

        ResponseEntity<List<ItemRequestFullDto>> response = controller.findAllMyRequests(anyLong());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemRequests, response.getBody());
    }

    @Test
    void findAllRequestsByUserId_whenInvoked_thenReturnStatusOkWithItemRequestCollectionBody() {
        long userId = 0L;
        Long from = 1L;
        Long size = 1L;
        List<ItemRequestFullDto> expectedItemRequests = List.of(new ItemRequestFullDto());
        when(requestService.findAllRequestsByOtherUsers(userId, from, size)).thenReturn(expectedItemRequests);

        ResponseEntity<List<ItemRequestFullDto>> response = controller.findAllRequestsByUserId(userId, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemRequests, response.getBody());
    }

    @Test
    void findById_whenInvoked_thenReturnStatusOkWithItemRequestBody() {
        long userId = 0L;
        long requestId = 0L;
        ItemRequestFullDto expectedItemRequest = new ItemRequestFullDto();
        when(requestService.findById(userId, requestId)).thenReturn(expectedItemRequest);

        ResponseEntity<ItemRequestFullDto> response = controller.findById(userId, requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItemRequest, response.getBody());
    }

}
