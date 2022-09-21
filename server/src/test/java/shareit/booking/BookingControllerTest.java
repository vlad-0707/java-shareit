package shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shareit.booking.dto.BookingConsumerDto;
import shareit.booking.dto.BookingDto;
import shareit.booking.model.Booking;
import shareit.booking.service.BookingService;
import shareit.item.model.Item;
import shareit.request.model.ItemRequest;
import shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    User owner = new User(
            1L,
            "Ivan",
            "Ivanov@yandex.ru");
    User requestor = new User(
            2L,
            "Egor",
            "Egorov@yandex.ru");
    ItemRequest firstItemRequest = new ItemRequest(
            1L,
            "Хочу воспользоваться дрелью",
            requestor,
            LocalDateTime.now());
    Item firstItem = new Item(
            1L,
            "Дрель",
            "Электрическая дрель",
            true,
            owner,
            firstItemRequest);
    BookingConsumerDto bookingConsumerDto = new BookingConsumerDto(
            1L,
            1L,
            LocalDateTime.now().plusDays(1L),
            LocalDateTime.now().plusDays(2L)
    );
    Booking booking = new Booking(
            1L,
            LocalDateTime.now().plusDays(1L),
            LocalDateTime.now().plusDays(2L),
            firstItem,
            requestor,
            Status.APPROVED
    );
    BookingDto bookingDto;

    @BeforeEach
    void getBookingDto() {
        bookingDto = modelMapper.map(booking, BookingDto.class);
    }

    @Test
    void addNewBooking() throws Exception {
        when(bookingService.create(any(), anyLong()))
                .thenReturn(booking);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingConsumerDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(bookingDto.getId()), Long.class));
    }

    @Test
    void approved() throws Exception {
        when(bookingService.approved(anyLong(), anyLong(), any()))
                .thenReturn(booking);
        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(bookingDto.getId()), Long.class));
    }

    @Test
    void getById() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenReturn(booking);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(bookingDto.getId()), Long.class));
    }
}