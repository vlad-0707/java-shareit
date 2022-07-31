package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final ItemStorage itemStorage;
    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public Item toItem(ItemDto itemDto, long userId) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName() != null ? itemDto.getName() : itemStorage.getItemById(itemDto.getId()).getName())
                .description(itemDto.getDescription() != null ?
                        itemDto.getDescription() : itemStorage.getItemById(itemDto.getId()).getDescription())
                .available(itemDto.getAvailable() != null ?
                        itemDto.getAvailable() : itemStorage.getItemById(itemDto.getId()).getAvailable())
                .owner(userId)
                .request(null)
                .build();
    }
}
