package shareit.request.service;


import shareit.exception.RequestNotFoundException;
import shareit.exception.RequestValidException;
import shareit.exception.UserNotFoundException;
import shareit.request.dto.ItemRequestDto;
import shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest addNewRequest(long userId, ItemRequestDto itemRequestDto) throws UserNotFoundException;

    List<ItemRequestDto> getRequestsByUserId(long userId) throws UserNotFoundException;

    List<ItemRequestDto> getAllRequests(long userId, int from, int size) throws RequestValidException;

    ItemRequestDto getRequest(long requestId, long userId) throws UserNotFoundException, RequestNotFoundException;
}
