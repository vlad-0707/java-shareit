package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingConsumerDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.page.OffsetLimitPageable;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public Booking create(BookingConsumerDto bookingConsumerDto, Long userId) throws ItemNotFoundException, UserNotFoundException, UserValidException, ItemAvailableException {
        userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Item item = itemRepository.findById(bookingConsumerDto.getItemId())
                .orElseThrow(ItemNotFoundException::new);
        if (!item.getAvailable()) {
            throw new ItemAvailableException();
        }
        if (userId.equals(item.getOwner().getId())) {
            throw new UserNotFoundException();
        }
        Booking booking = modelMapper.map(bookingConsumerDto, Booking.class);
        booking.setItem(modelMapper.map(itemRepository.findById(bookingConsumerDto.getItemId()), Item.class));
        booking.setBooker(userService.getById(userId));
        booking.setStatus(Status.WAITING);
        log.info("Create booking with Id: {}",booking.getId());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approved(Long userId, Long bookingId, Boolean approve) throws BookingNotFoundException, UserNotFoundException, ItemAvailableException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ItemAvailableException();
        }
        if (!itemRepository.findById(booking.getItem().getId()).get().getOwner().getId().equals(userId)) {
            throw new UserNotFoundException();
        }
        booking.setStatus(approve ? Status.APPROVED : Status.REJECTED);
        log.info("Approved status booking with status: {}", booking.getStatus());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getById(Long userId, Long bookingId) throws BookingNotFoundException, UserNotFoundException {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return booking;
        } else {
            throw new BookingNotFoundException();
        }
    }

    @Override
    public Page<Booking> getAllBookingsByUserId(Long userId, String state, int from, int size) throws UserNotFoundException, NotSupportException, RequestValidException {
        if (size < 1 || from < 0) {
            throw new RequestValidException();
        }
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "WAITING":
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                        userId, Status.WAITING, OffsetLimitPageable.of(from, size));
            case "REJECTED":
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                        userId, Status.REJECTED, OffsetLimitPageable.of(from, size));
            case "PAST":
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                        userId, now, OffsetLimitPageable.of(from, size));
            case "FUTURE":
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(
                        userId, now, OffsetLimitPageable.of(from, size));
            case "CURRENT":
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, now, now, OffsetLimitPageable.of(from, size));
            case "ALL":
                return bookingRepository.findAllByBookerIdOrderByStartDesc(
                        userId, OffsetLimitPageable.of(from, size));
            default:
                throw new NotSupportException();
        }
    }

    @Override
    public Page<Booking> getAllBookingsForOwner(Long userId, String state, int from, int size) throws UserNotFoundException, NotSupportException, RequestValidException {
        if (size < 1 || from < 0) {
            throw new RequestValidException();
        }
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "WAITING":
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                        userId, Status.WAITING, OffsetLimitPageable.of(from, size));
            case "REJECTED":
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                        userId, Status.REJECTED, OffsetLimitPageable.of(from, size));
            case "PAST":
                return bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(
                        userId, now, OffsetLimitPageable.of(from, size));
            case "FUTURE":
                return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(
                        userId, now, OffsetLimitPageable.of(from, size));
            case "CURRENT":
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, now, now, OffsetLimitPageable.of(from, size));
            case "ALL":
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(
                        userId, OffsetLimitPageable.of(from, size));
            default:
                throw new NotSupportException();
        }
    }
}
