package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemValidException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.service.ItemService;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    private final ModelMapper modelMapper;

    @PostMapping
    public ItemDto addNewItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemDto itemDto) throws UserNotFoundException, ItemValidException, ItemAvailableException {
        return modelMapper.map(itemService.addNewItem(itemDto, userId), ItemDto.class);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) throws UserNotFoundException, ItemValidException, ItemAvailableException, ItemNotFoundException {
        return modelMapper.map(itemService.updateItem(itemDto, itemId, userId), ItemDto.class);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId,
                                 @Valid @RequestBody CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") long userId) throws ItemNotFoundException, UserNotFoundException, ItemAvailableException {
        return itemService.addComment(itemId, commentDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemOwnerDto getId(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId) throws ItemNotFoundException, UserNotFoundException {
        return itemService.getId(itemId, userId);
    }

    @GetMapping
    public List<ItemOwnerDto> getAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItemsByOwnerId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") long userId,
                                @RequestParam String text) {
        return itemService.search(text).stream()
                .map(item -> modelMapper.map(item, ItemDto.class))
                .collect(Collectors.toList());
    }
}
