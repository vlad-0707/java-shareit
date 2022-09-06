package ru.practicum.shareit.request.service;

import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.exception.RequestValidException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest addNewRequest(long userId, ItemRequestDto itemRequestDto) throws UserNotFoundException;

    List<ItemRequestDto> getRequestsByUserId(long userId) throws UserNotFoundException;

    List<ItemRequestDto> getAllRequests(long userId, int from, int size) throws RequestValidException;

    ItemRequestDto getRequest(long requestId, long userId) throws UserNotFoundException, RequestNotFoundException;
}
