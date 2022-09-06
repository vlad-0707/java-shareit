package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                                       LocalDateTime beforeTime,
                                                                                       LocalDateTime afterTime,
                                                                                       Pageable pageable);

    Integer countByItemIdAndBookerIdAndStatusAndStartBefore(long itemId,
                                                            long userId,
                                                            Status waiting,
                                                            LocalDateTime time);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                                          LocalDateTime beforeTime,
                                                                                          LocalDateTime afterTime,
                                                                                          Pageable pageable);

    Page<Booking> findAllByBookerIdOrderByStartDesc(Long userId,
                                                              Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, Status waiting,
                                                                          Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime time,
                                                                             Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime time,
                                                                              Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime time,
                                                                          Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime time,
                                                                           Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, Status waiting,
                                                                       Pageable pageable);

    Page<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId,
                                                                 Pageable pageable);

    Booking getFirstByItemIdOrderByEndDesc(long itemId);

    Booking getFirstByItemIdOrderByStartAsc(long itemId);

}
