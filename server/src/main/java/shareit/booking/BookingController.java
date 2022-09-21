package shareit.booking;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import shareit.booking.dto.BookingConsumerDto;
import shareit.booking.dto.BookingDto;
import shareit.booking.service.BookingService;
import shareit.exception.*;

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
                                    @RequestBody BookingConsumerDto bookingConsumerDto) throws UserNotFoundException, ItemAvailableException, ItemNotFoundException, ItemValidException, UserValidException {
        return modelMapper.map(bookingService.create(bookingConsumerDto, userId), BookingDto.class);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approved(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long bookingId,
                               @RequestParam Boolean approved) throws UserNotFoundException, BookingNotFoundException, ItemAvailableException {
        return modelMapper.map(bookingService.approved(userId, bookingId, approved), BookingDto.class);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long bookingId) throws BookingNotFoundException, BookingValidException, UserNotFoundException {
        return modelMapper.map(bookingService.getById(userId, bookingId), BookingDto.class);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(required = false, defaultValue = "0") int from,
                                                   @RequestParam(required = false, defaultValue = "20") int size) throws UserNotFoundException, ItemAvailableException, NotSupportException, RequestValidException {
        return bookingService.getAllBookingsByUserId(userId, state, from, size).stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(required = false, defaultValue = "0") int from,
                                                   @RequestParam(required = false, defaultValue = "20") int size) throws UserNotFoundException, NotSupportException, RequestValidException {
        return bookingService.getAllBookingsForOwner(userId, state, from, size).stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }
}
