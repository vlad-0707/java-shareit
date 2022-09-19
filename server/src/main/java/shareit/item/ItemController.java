package shareit.item;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import shareit.exception.ItemAvailableException;
import shareit.exception.ItemNotFoundException;
import shareit.exception.ItemValidException;
import shareit.exception.UserNotFoundException;
import shareit.item.dto.CommentDto;
import shareit.item.dto.ItemDto;
import shareit.item.dto.ItemOwnerDto;
import shareit.item.service.ItemService;



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
                              @RequestBody ItemDto itemDto) throws UserNotFoundException, ItemValidException, ItemAvailableException {
        ItemDto itemDtoAfterSave = modelMapper.map(itemService.addNewItem(itemDto, userId), ItemDto.class);
        itemDtoAfterSave.setRequestId(itemDto.getRequestId());
        return itemDtoAfterSave;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) throws UserNotFoundException, ItemValidException, ItemAvailableException, ItemNotFoundException {
        return modelMapper.map(itemService.updateItem(itemDto, itemId, userId), ItemDto.class);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId,
                                 @RequestBody CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") long userId) throws ItemNotFoundException, UserNotFoundException, ItemAvailableException {
        return itemService.addComment(itemId, commentDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemOwnerDto getId(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId) throws ItemNotFoundException, UserNotFoundException {
        return itemService.getId(itemId, userId);
    }

    @GetMapping
    public List<ItemOwnerDto> getAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(required = false, defaultValue = "0") int from,
                                                   @RequestParam(required = false, defaultValue = "20") int size) {
        return itemService.getAllItemsByOwnerId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") long userId,
                                @RequestParam String text,
                                @RequestParam(required = false, defaultValue = "0") int from,
                                @RequestParam(required = false, defaultValue = "20") int size) {
        return itemService.search(text, from, size).stream()
                .map(item -> modelMapper.map(item, ItemDto.class))
                .collect(Collectors.toList());
    }
}
