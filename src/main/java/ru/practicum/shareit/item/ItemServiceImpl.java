package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Long userId) {
        log.info("Добавление вещи");
        if (userRepository.findById(userId).isEmpty() ) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
        Item item = ItemMapper.fromItemDto(itemDto, true, userId);
        itemRepository.save(item);
        return ItemMapper.toFullItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto update(ItemUpdateDto itemDto, Long userId, Long itemId) {
        log.info("Обновление вещи");
        if (userRepository.findById(userId).isEmpty() ) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
        List<Item> items = itemRepository.findAll();
        Optional<Item> itemO = itemRepository.findById(itemId);
        Item item;
        if (itemDto.getAvailable() != null) {
            if (itemDto.getAvailable().equals("true")) {
                item = ItemMapper.fromItemUpdateDto(itemDto, true, userId, itemId);
            } else {
                item = ItemMapper.fromItemUpdateDto(itemDto, false, userId, itemId);
            }
        } else {
            item = ItemMapper.fromItemUpdateDto(itemDto, itemO.orElseThrow().isAvailable(), userId, itemId);
        }
        if (userId == itemO.orElseThrow().getOwnerId()) {
            isValidateItem(item, itemO.orElseThrow());
        }
        return ItemMapper
                .toFullItemDto(itemRepository
                        .saveAndFlush(itemO.orElseThrow()));
    }

    private void isValidateItem(Item item, Item itemToChange) {
        if (item.getName() != null) {
            itemToChange.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemToChange.setDescription(item.getDescription());
        }
        if (item.isAvailable()) {
            itemToChange.setAvailable(true);
        }
        if (!item.isAvailable()) {
            itemToChange.setAvailable(false);
        }
    }

    @Transactional
    @Override
    public ItemDtoWithBookingDates findItemById(Long id, Long userId) {
        log.info("Поиск вещи по id");
        List<Item> items = itemRepository.findAll();
        Optional<Item> itemO = itemRepository.findById(id);
        if (itemO.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        } else {
            List<Booking> bookingsAllStatus = bookingRepository.findAllByItemIdOrderByStartDesc(id);
            List<Booking> bookings = new ArrayList<>();
            for (Booking allStatus : bookingsAllStatus) {
                if (!allStatus.getStatus().equals(BookingStatus.REJECTED)) {
                    bookings.add(allStatus);
                }
            }
            Optional<Booking> lastBooking = Optional.empty();
            Optional<Booking> nextBooking = Optional.empty();
            Long itemId = itemO.get().getId();
            List<Comment> com = commentRepository.findAllByItemId(itemId);
            List<Comment> comments = commentRepository.findAll();
            List<Comment> comment = new ArrayList<>();
            if (!comments.isEmpty()) {
                comment = commentRepository.findAllByItemId(itemO.get().getId());
            }
            for (Booking booking : bookings) {
                if (booking.getEnd().isBefore(LocalDateTime.now())) {
                    if (lastBooking.isPresent()) {
                        if (booking.getEnd().isAfter(lastBooking.get().getEnd())) {
                            lastBooking = Optional.of(booking);
                        }
                    } else {
                        lastBooking = Optional.of(booking);
                    }
                } else {
                    if (nextBooking.isPresent()) {
                        if (booking.getEnd().isBefore(nextBooking.get().getEnd())) {
                            nextBooking = Optional.of(booking);
                        }
                    } else {
                        nextBooking = Optional.of(booking);
                    }
                }
            }
            if ((lastBooking.isEmpty()) && (nextBooking.isEmpty()) || (itemO.get().getOwnerId() != userId)) {
                return ItemMapper.toItemDtoWithBookingDates(
                        itemO.get(),
                        null,
                        null,
                        comment
                );
            } else if (lastBooking.isEmpty()) {
                return ItemMapper.toItemDtoWithBookingDates(
                        itemO.get(),
                        null,
                        nextBooking.get(),
                        comment
                );
            } else if (nextBooking.isEmpty()) {
                return ItemMapper.toItemDtoWithBookingDates(
                        itemO.get(),
                        lastBooking.get(),
                        null,
                        comment
                );
            } else {
                return ItemMapper.toItemDtoWithBookingDates(
                        itemO.get(),
                        lastBooking.get(),
                        nextBooking.get(),
                        comment
                );
            }
        }
    }

    @Override
    public List<ItemDto> getAll() {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : itemRepository.findAll()) {
            itemsDto.add(ItemMapper.toFullItemDto(item));
        }
        return itemsDto;
    }

    @Transactional
    @Override
    public List<ItemDtoWithBookingDates> getAllByUserId(Long userId) {
        List<Item> itemsToCheck = itemRepository.findAll();
        List<ItemDtoWithBookingDates> itemsByUser = new ArrayList<>();
        for (Item item : itemsToCheck) {
            if (item.getOwnerId() == userId) {
                List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStartDesc(item.getId());
                Optional<Booking> lastBooking = Optional.empty();
                Optional<Booking> nextBooking = Optional.empty();
                List<Comment> comments = commentRepository.findAll();
                List<Comment> comment = new ArrayList<>();
                if (!comments.isEmpty()) {
                    comment = commentRepository.findAllByItemId(item.getId());
                }
                for (Booking booking : bookings) {
                    if (booking.getEnd().isBefore(LocalDateTime.now())) {
                        if (lastBooking.isPresent()) {
                            if (booking.getEnd().isAfter(lastBooking.get().getEnd())) {
                                lastBooking = Optional.of(booking);
                            }
                        } else {
                            lastBooking = Optional.of(booking);
                        }
                    } else {
                        if (nextBooking.isPresent()) {
                            if (booking.getEnd().isBefore(nextBooking.get().getEnd())) {
                                nextBooking = Optional.of(booking);
                            }
                        } else {
                            nextBooking = Optional.of(booking);
                        }
                    }
                }
                if (((lastBooking.isEmpty())&&(nextBooking.isEmpty()))||(item.getOwnerId() != userId)) {
                    itemsByUser.add(ItemMapper.toItemDtoWithBookingDates(
                            item,
                            null,
                            null,
                            comment
                    ));
                } else if (lastBooking.isEmpty()) {
                    itemsByUser.add(ItemMapper.toItemDtoWithBookingDates(
                            item,
                            null,
                            nextBooking.get(),
                            comment
                    ));
                } else if (nextBooking.isEmpty()) {
                    itemsByUser.add(ItemMapper.toItemDtoWithBookingDates(
                            item,
                            lastBooking.get(),
                            null,
                            comment
                    ));
                } else {
                    itemsByUser.add(ItemMapper.toItemDtoWithBookingDates(
                            item,
                            lastBooking.get(),
                            nextBooking.get(),
                            comment
                    ));
                }
            }
        }
        return itemsByUser;
    }

    @Transactional
    @Override
    public List<ItemDto> search(String text) {
        log.info("Поиск вещи");
        List<ItemDto> checked = new ArrayList<>();
        if (!text.isBlank()) {
            for (Item item : itemRepository.findItemsByQuery(text)) {
                if (item.isAvailable()) {
                    checked.add(ItemMapper.toFullItemDto(item));
                }
            }
        }
        return checked;
    }

    @Transactional
    @Override
    public CommentFullDto createComment(CommentDto commentSto, Long itemId, Long userId) {
        List<Item> items = itemRepository.findAll();
        Optional<Item> item = itemRepository.findById(itemId);
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        List<User> user = userRepository.findAll();
        if (bookings.isEmpty()) {
            throw new NotFoundException("У вас нет ни одного бронирования");
        }
        for (Booking booking : bookings) {
            if ((booking.getItem().getId().equals(itemId)) &&
                    (!booking.getStatus().equals(BookingStatus.REJECTED)) &&
                    (booking.getEnd().isBefore(LocalDateTime.now()))) {
                Comment comment = CommentMapper.fromCommentDto(
                        commentSto,
                        itemRepository.findById(itemId).get(),
                        userRepository.findById(userId).get()
                );
                Comment savedComment = commentRepository.save(comment);
                return CommentMapper.toCommentFullDto(savedComment, LocalDateTime.now());
            }
        }
        throw new ValidationException("У вас не было подтвержденного бронирования вещи с id :" + itemId);
    }
}