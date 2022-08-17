package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingConsumerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private final ModelMapper modelMapper;

    @PostMapping
    public BookingDto addNewBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @Valid @RequestBody BookingConsumerDto bookingConsumerDto)
            throws UserNotFoundException, ItemAvailableException,
            ItemNotFoundException, ItemValidException, UserValidException {
        return modelMapper.map(bookingService.create(bookingConsumerDto, userId), BookingDto.class);
    }
    @PatchMapping("/{bookingId}")
    public BookingDto approved(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long bookingId,
                               @RequestParam Boolean approved) throws UserNotFoundException,
            BookingNotFoundException, ItemAvailableException {
        return modelMapper.map(bookingService.approved(userId, bookingId, approved), BookingDto.class);
    }
    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long bookingId) throws BookingNotFoundException,
            BookingValidException, UserNotFoundException {
        return modelMapper.map(bookingService.getById(userId, bookingId), BookingDto.class);
    }
    @GetMapping
    public List<BookingDto> getAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state)
            throws UserNotFoundException, ItemAvailableException, NotSupportException {
        return bookingService.getAllBookingsByUserId(userId, state).stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }
    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state)
            throws UserNotFoundException, NotSupportException {
        return bookingService.getAllBookingsForOwner(userId, state).stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }
}
