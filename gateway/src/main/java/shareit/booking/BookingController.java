package shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.booking.dto.BookingConsumerDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;


	@PostMapping
	public ResponseEntity<Object> addNewBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
									@Valid @RequestBody BookingConsumerDto bookingConsumerDto)  {
		log.info("Added new booking with user id: {}",userId);
		return bookingClient.create(bookingConsumerDto, userId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approved(@RequestHeader("X-Sharer-User-Id") Long userId,
							   @PathVariable Long bookingId,
							   @RequestParam Boolean approved)  {
		log.info("Approved booking with id: {}",bookingId);
		return bookingClient.approved(userId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
							  @PathVariable Long bookingId)  {
		log.info("Found booking with id: {}",bookingId);
		return bookingClient.getById(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
												   @RequestParam(defaultValue = "ALL") String state,
												   @RequestParam(required = false, defaultValue = "0") int from,
												   @RequestParam(required = false, defaultValue = "20") int size)  {
		return bookingClient.getAllBookingsByUserId(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
												   @RequestParam(defaultValue = "ALL") String state,
												   @RequestParam(required = false, defaultValue = "0") int from,
												   @RequestParam(required = false, defaultValue = "20") int size) {
		return bookingClient.getAllBookingsForOwner(userId, state, from, size);
	}
}
