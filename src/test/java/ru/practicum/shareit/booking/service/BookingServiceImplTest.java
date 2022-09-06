package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingConsumerDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@SpringBootTest
class BookingServiceImplTest {

    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ItemServiceImpl itemService;


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
    ItemRequest secondItemRequest = new ItemRequest(
            2L,
            "Пазязя дайте молоток",
            requestor,
            LocalDateTime.now());
    Item firstItem = new Item(
            1L,
            "Дрель",
            "Электрическая дрель",
            true,
            owner,
            firstItemRequest);
    Item secondItem = new Item(
            2L,
            "Молоток",
            "Молоток с деревянной ручкой",
            true,
            owner,
            secondItemRequest);

    BookingConsumerDto booking = new BookingConsumerDto(
            1L,
            1L,
            LocalDateTime.now(),
            LocalDateTime.now()
    );

    @BeforeEach
    void init() throws UserNotFoundException, ItemAvailableException {
        userService.create(modelMapper.map(owner, UserDto.class));
        userService.create(modelMapper.map(requestor, UserDto.class));
        itemService.addNewItem(modelMapper.map(this.firstItem, ItemDto.class), owner.getId());
        itemService.addNewItem(modelMapper.map(this.secondItem, ItemDto.class), owner.getId());
    }


    @Test
    void create() throws UserNotFoundException, ItemAvailableException, UserValidException, ItemNotFoundException {
        Booking booking = bookingService.create(this.booking, requestor.getId());
        Assertions.assertEquals(this.booking.getId(), booking.getId());
    }


    @Test
    void approved() throws UserNotFoundException, BookingNotFoundException, ItemAvailableException, UserValidException, ItemNotFoundException {
        bookingService.create(this.booking, requestor.getId());
        Booking booking = bookingService.approved(owner.getId(), this.booking.getId(), true);
        Assertions.assertEquals(Status.APPROVED, booking.getStatus());

    }

    @Test
    void getById() throws UserNotFoundException, ItemAvailableException, UserValidException, ItemNotFoundException, BookingNotFoundException {
        Booking booking = bookingService.create(this.booking, requestor.getId());
        Assertions.assertEquals(booking, bookingService.getById(owner.getId(), this.booking.getId()));
    }

    @Test
    void getAllBookingsByUserId() throws UserNotFoundException, ItemAvailableException, UserValidException, ItemNotFoundException, NotSupportException, RequestValidException {
        Booking booking = bookingService.create(this.booking, requestor.getId());
        Assertions.assertEquals(List.of(booking), bookingService.getAllBookingsByUserId(requestor.getId(),
                        "WAITING",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
        Assertions.assertEquals(new ArrayList(), bookingService.getAllBookingsByUserId(requestor.getId(),
                        "REJECTED",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
        Assertions.assertEquals(List.of(booking), bookingService.getAllBookingsByUserId(requestor.getId(),
                        "PAST",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
        Assertions.assertEquals(new ArrayList(), bookingService.getAllBookingsByUserId(requestor.getId(),
                        "FUTURE",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
        Assertions.assertEquals(new ArrayList(), bookingService.getAllBookingsByUserId(requestor.getId(),
                        "CURRENT",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
        Assertions.assertEquals(List.of(booking), bookingService.getAllBookingsByUserId(requestor.getId(),
                        "ALL",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
    }

    @Test
    void getAllBookingsForOwner() throws UserNotFoundException, ItemAvailableException, UserValidException, ItemNotFoundException, NotSupportException, RequestValidException {
        Booking booking = bookingService.create(this.booking, requestor.getId());
        Assertions.assertEquals(List.of(booking), bookingService.getAllBookingsForOwner(owner.getId(),
                        "WAITING",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
        Assertions.assertEquals(new ArrayList(), bookingService.getAllBookingsForOwner(owner.getId(),
                        "REJECTED",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
        Assertions.assertEquals(List.of(booking), bookingService.getAllBookingsForOwner(owner.getId(),
                        "PAST",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
        Assertions.assertEquals(new ArrayList(), bookingService.getAllBookingsForOwner(owner.getId(),
                        "FUTURE",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
        Assertions.assertEquals(new ArrayList(), bookingService.getAllBookingsForOwner(owner.getId(),
                        "CURRENT",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
        Assertions.assertEquals(List.of(booking), bookingService.getAllBookingsForOwner(owner.getId(),
                        "ALL",
                        0,
                        1)
                .stream()
                .collect(Collectors.toList()));
    }
}