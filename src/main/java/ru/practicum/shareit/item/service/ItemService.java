package ru.practicum.shareit.item.service;

import ru.practicum.shareit.booking.dto.NestedBookingDto;
import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemValidException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addNewItem(ItemDto itemDto, long userId) throws UserNotFoundException, ItemValidException, ItemAvailableException;

    Item updateItem(ItemDto itemDto, long itemId, long userId) throws UserNotFoundException, ItemValidException, ItemAvailableException, ItemNotFoundException;

    ItemOwnerDto getId(long itemId, long userId) throws ItemNotFoundException, UserNotFoundException;

    List<ItemOwnerDto> getAllItemsByOwnerId(long id, int from, int size);

    List<Item> search(String text, int from, int size);

    NestedBookingDto getLastByItemId(long itemId);

    NestedBookingDto getNextByItemId(long itemId);

    CommentDto addComment(long itemId, CommentDto commentDto, long userId) throws ItemNotFoundException, UserNotFoundException, ItemAvailableException;

    List<CommentDto> getComments(long itemId);

    boolean isHasBookingsByItemIdAndUserId(long itemId, long userId);
}

