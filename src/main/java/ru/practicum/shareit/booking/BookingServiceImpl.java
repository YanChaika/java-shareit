package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;


import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingFullDto create(BookingDto bookingDto, Long userId) {
        log.info("Добавление бронирования");
        LocalDateTime instant = LocalDateTime.now();
        if (bookingDto.getStart().isBefore(instant)
                || bookingDto.getEnd().isBefore(instant)
                || bookingDto.getEnd().isBefore(bookingDto.getStart())
                || bookingDto.getEnd().equals(bookingDto.getStart()))
        {
            throw new ValidationException("Время бронирования прошло");
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден")
        );
        List<Item> items = itemRepository.findAll();
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(
                () -> new NotFoundException("Item с id " + bookingDto.getItemId() + " не найден")
        );
        if (item.getOwnerId().equals(userId)) {
            throw new NotFoundException("owner can't booking his item");
        }
        if (!item.isAvailable()) {
            throw new ValidationException("Item не доступен для бронирования");
        }
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setBookerId(userId);
        Booking booking = BookingMapper.fromBookingDto(bookingDto, user, item);
        Booking bookingWithId = bookingRepository.save(booking);
        BookingFullDto bookingFullDto = BookingMapper
                .toBookingFullDto(bookingWithId, bookingWithId.getBooker(), bookingWithId.getItem());
        return bookingFullDto;
    }

    @Transactional
    @Override
    public BookingFullDto update(String approved, Long bookingId, Long userId) {
        log.info("Обновление бронирования");
        Booking oldBooking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Booking с id " + bookingId + " не найден")
        );
        if (oldBooking.getItem().getOwnerId().equals(userId)) {
            if (approved.equals("true")) {
                if (oldBooking.getStatus().equals(BookingStatus.APPROVED)) {
                    throw new ValidationException("Booking was approved earlier");
                }
                oldBooking.setStatus(BookingStatus.APPROVED);
            } else {
                oldBooking.setStatus(BookingStatus.REJECTED);
            }
        } else {
            throw new NotFoundException("you are not owner");
        }
        Booking bookingToSave = bookingRepository.save(oldBooking);
        return BookingMapper.toBookingFullDto(bookingToSave, oldBooking.getBooker(), oldBooking.getItem());
    }

    @Override
    public BookingFullDto get(Long userId, Long bookingId) {
        log.info("Получение бронирования");
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден")
        );
        Optional<Booking> bookingO = bookingRepository.findById(bookingId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Booking с id " + bookingId + " не найден")
        );
        if ((booking.getBooker().getId().equals(userId)) || (booking.getItem().getOwnerId().equals(userId))) {
            return BookingMapper.toBookingFullDto(booking, booking.getBooker(), booking.getItem());
        }
        throw new NotFoundException("user id не соответствует id владельца или бронирующего");
    }

    @Transactional
    @Override
    public List<BookingFullDto> getByUserIdAndState(Long userId, BookingState state) {
        log.info("Получение бронирования по id");
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден")
        );
        List<Item> items = itemRepository.findAll();
        List<Booking> bookingsByUser = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        List<Booking> sortedBookings = sortedByState(bookingsByUser, state, LocalDateTime.now());
        return BookingMapper.foBookingsFullDto(sortedBookings);
    }

    @Override
    public List<BookingFullDto> getAllBookingByItemsForUserId(Long userId, BookingState state) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User с id " + userId + " не найден")
        );
        List<Item> items = itemRepository.findItemsByOwnerId(userId);
        List<Booking> bookings = new ArrayList<>();
        for (Item item : items) {
            bookings.addAll(bookingRepository.findAllByItemIdOrderByStartDesc(item.getId()));
        }
        List<Booking> sortedBookings = sortedByState(bookings, state, LocalDateTime.now());
        return BookingMapper.foBookingsFullDto(sortedBookings);
    }

    private List<Booking> sortedByState(List<Booking> bookings, BookingState state, LocalDateTime time) {
        List<Booking> sortedBooking = new ArrayList<>();
        if (state.equals(BookingState.ALL)) {
            return bookings;
        } else if (state.equals(BookingState.FUTURE)) {
            for (Booking booking : bookings) {
                if ((booking.getStart().isAfter(time)) && (!booking.getStatus().equals(BookingStatus.REJECTED))) {
                    sortedBooking.add(booking);
                }
            }
            return sortedBooking;
        } else if (state.equals(BookingState.PAST)) {
            for (Booking booking : bookings) {
                if ((booking.getEnd().isBefore(time)) && (!booking.getStatus().equals(BookingStatus.REJECTED))) {
                    sortedBooking.add(booking);
                }
            }
            return sortedBooking;
        } else if (state.equals(BookingState.CURRENT)) {
            for (Booking booking : bookings) {
                if ((booking.getEnd().isAfter(time)) &&
                        (booking.getStart().isBefore(time))) {
                    sortedBooking.add(booking);
                }
            }
            return sortedBooking;
        } else {
            for (Booking booking : bookings) {
                if (booking.getStatus().toString().equals(state.toString())) {
                    sortedBooking.add(booking);
                }
            }
            return sortedBooking;
        }
    }

    @Override
    public List<BookingFullDto> getAllByUserId(Long userId) {
        log.info("Получение бронирования по user id");
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден")
        );
        List<Item> items = itemRepository.findAll();
        List<Booking> bookingsByUser = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        return BookingMapper.foBookingsFullDto(bookingsByUser);
    }
}
