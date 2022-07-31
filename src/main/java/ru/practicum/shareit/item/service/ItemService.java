package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.exception.ItemValidException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(ItemDto itemDto, long userId) throws UserNotFoundException, ItemValidException, ItemAvailableException;
    ItemDto updateItem(ItemDto itemDto, long itemId, long userId)
            throws UserNotFoundException, ItemValidException, ItemAvailableException;
    ItemDto getId(long itemId);
    List<ItemDto> getAllItemsByOwnerId(long id);
    List<ItemDto> search(String text);
}
