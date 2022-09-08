package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.exception.RequestValidException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;
@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ModelMapper modelMapper;

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addNewRequest(@RequestHeader("X-Sharer-User-Id")long userId,
                                        @Valid @RequestBody ItemRequestDto itemRequestDto) throws UserNotFoundException {
        log.info("New request with userId:{} added", userId);
        return modelMapper.map(itemRequestService.addNewRequest(userId, itemRequestDto), ItemRequestDto.class);
    }

    @GetMapping
    public List<ItemRequestDto> getRequestsByUserId(@RequestHeader("X-Sharer-User-Id")long userId) throws UserNotFoundException {
        log.info("Request by userId: {}", userId);
        return itemRequestService.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id")long userId,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "20") int size) throws RequestValidException, UserNotFoundException {
        log.info("All requests by userId: {}", userId);
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getRequest(@RequestHeader("X-Sharer-User-Id")long userId,
                                     @PathVariable long requestId) throws UserNotFoundException, RequestNotFoundException {
        log.info("Request by requestId: {}", requestId);
        return itemRequestService.getRequest(requestId, userId);
    }
}
