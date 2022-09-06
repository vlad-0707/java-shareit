package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.exception.RequestValidException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ModelMapper modelMapper;

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addNewRequest(@RequestHeader("X-Sharer-User-Id")long userId,
                                        @Valid @RequestBody ItemRequestDto itemRequestDto) throws UserNotFoundException {
        return modelMapper.map(itemRequestService.addNewRequest(userId, itemRequestDto), ItemRequestDto.class);
    }

    @GetMapping
    public List<ItemRequestDto> getRequestsByUserId(@RequestHeader("X-Sharer-User-Id")long userId) throws UserNotFoundException {
        return itemRequestService.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id")long userId,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "20") int size) throws RequestValidException, UserNotFoundException {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getRequest(@RequestHeader("X-Sharer-User-Id")long userId,
                                     @PathVariable long requestId) throws UserNotFoundException, RequestNotFoundException {
        return itemRequestService.getRequest(requestId, userId);

    }
}
