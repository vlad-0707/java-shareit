package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.exception.RequestValidException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@SpringBootTest
class ItemRequestServiceImplTest {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ItemRequestServiceImpl itemRequestService;
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

    Item firstItem = new Item(
            1L,
            "Дрель",
            "Электрическая дрель",
            true,
            owner,
            firstItemRequest);

    @BeforeEach
    void init() {
        userService.create(modelMapper.map(owner, UserDto.class));
        userService.create(modelMapper.map(requestor, UserDto.class));
    }

    @Test
    void addNewRequest() throws UserNotFoundException {
        ItemRequest itemRequest = itemRequestService.addNewRequest(requestor.getId(),
                modelMapper.map(firstItemRequest, ItemRequestDto.class));

        Assertions.assertEquals(firstItemRequest.getDescription(), itemRequest.getDescription());

    }

    @Test
    void getRequestsByUserId() throws UserNotFoundException {
        ItemRequest itemRequest = itemRequestService.addNewRequest(requestor.getId(),
                modelMapper.map(firstItemRequest, ItemRequestDto.class));
        ItemRequestDto newItemRequest = modelMapper.map(itemRequest, ItemRequestDto.class);
        newItemRequest.setItems(new ArrayList<>());

        List<ItemRequestDto> itemRequestsDto = itemRequestService.getRequestsByUserId(requestor.getId());

        Assertions.assertEquals(Stream.of(newItemRequest)
                        .map(ItemRequestDto::getDescription)
                        .collect(Collectors.toList()),
                itemRequestsDto.stream()
                        .map(ItemRequestDto::getDescription)
                        .collect(Collectors.toList()));
    }

    @Test
    void getAllRequests() throws UserNotFoundException, RequestValidException {
        ItemRequest itemRequest = itemRequestService.addNewRequest(requestor.getId(),
                modelMapper.map(firstItemRequest, ItemRequestDto.class));
        ItemRequestDto newItemRequest = modelMapper.map(itemRequest, ItemRequestDto.class);
        newItemRequest.setItems(new ArrayList<>());

        List<ItemRequestDto> itemRequestsDto = itemRequestService.getAllRequests(owner.getId(), 0,1);

        Assertions.assertEquals(Stream.of(newItemRequest)
                        .map(ItemRequestDto::getDescription)
                        .collect(Collectors.toList()),
                itemRequestsDto.stream()
                        .map(ItemRequestDto::getDescription)
                        .collect(Collectors.toList()));
    }

    @Test
    void getRequest() throws UserNotFoundException, RequestNotFoundException {
        ItemRequest itemRequest = itemRequestService.addNewRequest(requestor.getId(),
                modelMapper.map(firstItemRequest, ItemRequestDto.class));
        ItemRequestDto newItemRequest = modelMapper.map(itemRequest, ItemRequestDto.class);
        newItemRequest.setItems(new ArrayList<>());

        Assertions.assertEquals(newItemRequest.getDescription(), itemRequestService.getRequest(
                firstItemRequest.getId(), requestor.getId()).getDescription());
    }
}