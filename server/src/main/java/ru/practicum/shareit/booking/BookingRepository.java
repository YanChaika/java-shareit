package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    Page<Booking> findAllByBookerIdOrderByEndDesc(Long userId, Pageable pageable);

    List<Booking> findAllByItemIdOrderByIdAsc(Long itemId);

    Page<Booking> findAllByItemIdOrderByIdAsc(Long itemId, Pageable pageable);

}
