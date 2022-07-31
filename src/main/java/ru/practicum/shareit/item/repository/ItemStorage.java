package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item addNewItem(Item item);
    void updateItem(Item item);
    Item getId(long itemId);
    List<Item> getAllItemsByOwnerId(long id);
    List<Item> search(String text);
    void validAvailable(ItemDto itemDto) throws ItemAvailableException;
    void validOwner(long itemId, long userId) throws UserNotFoundException;
}
