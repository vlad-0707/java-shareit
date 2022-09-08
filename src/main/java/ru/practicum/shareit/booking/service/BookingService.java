package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.dto.BookingConsumerDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.*;

public interface BookingService {

    Booking create(BookingConsumerDto bookingConsumerDto, Long userId) throws UserNotFoundException, ItemAvailableException, ItemNotFoundException, ItemValidException, UserValidException;

    Booking approved(Long userId, Long bookingId, Boolean approve) throws BookingNotFoundException, UserNotFoundException, ItemAvailableException;

    Booking getById(Long userId ,Long bookingId) throws BookingNotFoundException, BookingValidException, UserNotFoundException;

    Page<Booking> getAllBookingsByUserId(Long userId, String state, int from, int size) throws UserNotFoundException, ItemAvailableException, NotSupportException, RequestValidException;

    Page<Booking> getAllBookingsForOwner(Long userId, String state, int from, int size) throws UserNotFoundException, NotSupportException, RequestValidException;

}
