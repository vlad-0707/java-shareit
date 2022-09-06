package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingConsumerDto;
import ru.practicum.shareit.booking.dto.NestedBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Transactional
@SpringBootTest
class ItemServiceImplTest {

    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private BookingServiceImpl bookingService;

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
    CommentDto comment = new CommentDto(
            1L,
            "Роскошная дрель",
            requestor.getName(),
            LocalDateTime.now());

    BookingConsumerDto booking = new BookingConsumerDto(
            1L,
            1L,
            LocalDateTime.now(),
            LocalDateTime.now()
            );


    @BeforeEach
    void init() {
        userService.create(modelMapper.map(owner, UserDto.class));
        userService.create(modelMapper.map(requestor, UserDto.class));
    }

    @Test
    void addNewItem() throws UserNotFoundException, ItemAvailableException {

        Item newItem = itemService.addNewItem(modelMapper.map(firstItem, ItemDto.class), owner.getId());
        Assertions.assertEquals(firstItem.getName(), newItem.getName());
    }

    @Test
    void updateItem() throws UserNotFoundException, ItemAvailableException, ItemNotFoundException {
        Item item = new Item(
                1L,
                "Шуруповёрт",
                "Электрический шуруповёрт",
                true,
                owner,
                firstItemRequest);
        itemService.addNewItem(modelMapper.map(this.firstItem, ItemDto.class), owner.getId());
        Item newItem = itemService.updateItem(modelMapper.map(item, ItemDto.class), item.getId(), owner.getId());

        Assertions.assertEquals(item, newItem);
    }

    @Test
    void getId() throws UserNotFoundException, ItemAvailableException {
        Item item = itemService.addNewItem(modelMapper.map(this.firstItem, ItemDto.class), owner.getId());
        ItemOwnerDto newItem = modelMapper.map(item, ItemOwnerDto.class);
        newItem.setComments(new ArrayList<>());

        Assertions.assertEquals(newItem, itemService.getId(item.getId(), owner.getId()));
    }

    @Test
    void getAllItemsByOwnerId() throws UserNotFoundException, ItemAvailableException {

        Item firstItem = itemService.addNewItem(modelMapper.map(this.firstItem, ItemDto.class), owner.getId());
        Item secondItem = itemService.addNewItem(modelMapper.map(this.secondItem, ItemDto.class), owner.getId());

        ItemOwnerDto firstItemOwnerDto = modelMapper.map(firstItem, ItemOwnerDto.class);
        ItemOwnerDto secondItemOwnerDto = modelMapper.map(secondItem, ItemOwnerDto.class);
        firstItemOwnerDto.setComments(new ArrayList<>());
        secondItemOwnerDto.setComments(new ArrayList<>());

        Assertions.assertEquals(Arrays.asList(firstItemOwnerDto, secondItemOwnerDto),
                itemService.getAllItemsByOwnerId(owner.getId(), 0, 2));

    }

    @Test
    void search() throws UserNotFoundException, ItemAvailableException {

        Item firstItem = itemService.addNewItem(modelMapper.map(this.firstItem, ItemDto.class), owner.getId());
        Assertions.assertEquals(List.of(firstItem), itemService.search("ЭлЕкТрИчЕсКаЯ", 0, 1));
    }

    @Test
    void addComment() throws ItemAvailableException, UserNotFoundException, UserValidException, ItemNotFoundException, BookingNotFoundException {
        itemService.addNewItem(modelMapper.map(this.firstItem, ItemDto.class), owner.getId());
        Booking booking = bookingService.create(this.booking, requestor.getId());
        bookingService.approved(owner.getId(), booking.getId(), true);
        CommentDto comment = itemService.addComment(firstItem.getId(), this.comment, requestor.getId());
        Assertions.assertEquals(this.comment, comment);


    }

    @Test
    void isHasBookingsByItemIdAndUserId() throws UserNotFoundException, ItemAvailableException, UserValidException, ItemNotFoundException, BookingNotFoundException {
        itemService.addNewItem(modelMapper.map(this.firstItem, ItemDto.class), owner.getId());
        Booking booking = bookingService.create(this.booking, requestor.getId());
        bookingService.approved(owner.getId(), booking.getId(), true);
        Assertions.assertTrue(itemService.isHasBookingsByItemIdAndUserId(owner.getId(), requestor.getId()));

    }

    @Test
    void getLastByItemId() throws UserNotFoundException, ItemAvailableException, UserValidException, ItemNotFoundException, BookingNotFoundException {
        itemService.addNewItem(modelMapper.map(this.firstItem, ItemDto.class), owner.getId());
        Booking booking = bookingService.create(this.booking, requestor.getId());
        bookingService.approved(owner.getId(), booking.getId(), true);
        Assertions.assertEquals(booking.getId(), modelMapper.map(itemService.getLastByItemId(owner.getId()), NestedBookingDto.class).getId());
    }

    @Test
    void getNextByItemId() throws UserNotFoundException, BookingNotFoundException, ItemAvailableException, UserValidException, ItemNotFoundException {
        itemService.addNewItem(modelMapper.map(this.firstItem, ItemDto.class), owner.getId());
        Booking booking = bookingService.create(this.booking, requestor.getId());
        bookingService.approved(owner.getId(), booking.getId(), true);
        Assertions.assertEquals(booking.getId(), modelMapper.map(itemService.getNextByItemId(owner.getId()), NestedBookingDto.class).getId());
    }

    @Test
    void getComments() throws ItemAvailableException, UserNotFoundException, UserValidException, ItemNotFoundException, BookingNotFoundException {
        itemService.addNewItem(modelMapper.map(this.firstItem, ItemDto.class), owner.getId());
        Booking booking = bookingService.create(this.booking, requestor.getId());
        bookingService.approved(owner.getId(), booking.getId(), true);
        CommentDto comment = itemService.addComment(firstItem.getId(), this.comment, requestor.getId());
        Assertions.assertEquals(List.of(comment), itemService.getComments(firstItem.getId()));
    }
}