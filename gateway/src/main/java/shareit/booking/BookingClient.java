package shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.booking.dto.BookingConsumerDto;
import shareit.client.BaseClient;


import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(BookingConsumerDto bookingConsumerDto, Long userId) {
        return post("", userId, bookingConsumerDto);
    }

    public ResponseEntity<Object> approved(Long userId, Long bookingId, Boolean approved) {
        Map<String, Object> param = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved={approved}",userId , param, approved);
    }

    public ResponseEntity<Object> getById(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBookingsByUserId(Long userId, String state, int from, int size) {
        Map<String, Object> param = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, param);
    }

    public ResponseEntity<Object> getAllBookingsForOwner(Long userId, String state, int from, int size) {
        Map<String, Object> param = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, param);
    }
}
