package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.NestedBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemAvailableException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.page.OffsetLimitPageable;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public Item addNewItem(ItemDto itemDto, long userId) throws ItemAvailableException, UserNotFoundException {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (Objects.isNull(itemDto.getAvailable())) {
            throw new ItemAvailableException();
        }
        Item item = modelMapper.map(itemDto, Item.class);
        item.setOwner(userService.getById(userId));
        if(itemDto.getRequestId() != null){
            item.setRequest(itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow());
        }
        log.info("Added item {}", item.getName());
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(ItemDto itemDto, long itemId, long userId) throws ItemNotFoundException {
        Item item = itemRepository.findById(itemId).get();
        if (!item.getOwner().getId().equals(userId)) {
            throw new ItemNotFoundException();
        }
        modelMapper.map(itemDto, item);
        log.info("Updated item {}", item.getName());
        return itemRepository.save(item);
    }

    @Override
    public ItemOwnerDto getId(long itemId, long userId) throws UserNotFoundException {
        Item item = itemRepository.findById(itemId).orElseThrow(UserNotFoundException::new);
        ItemOwnerDto itemOwnerDto = modelMapper.map(item, ItemOwnerDto.class);
        itemOwnerDto.setComments(getComments(itemId));
        if (item.getOwner().getId().equals(userId)) {
            itemOwnerDto.setLastBooking(getLastByItemId(itemId));
            itemOwnerDto.setNextBooking(getNextByItemId(itemId));
        }
        log.info("Find owner item {}", itemOwnerDto.getName());
        return itemOwnerDto;
    }

    @Override
    public List<ItemOwnerDto> getAllItemsByOwnerId(long id, int from, int size) {
        return itemRepository.findAllByOwnerIdOrderByIdAsc(id, OffsetLimitPageable.of(from, size)).stream()
                .map(item -> {
                    ItemOwnerDto itemOwnerDto = modelMapper.map(item, ItemOwnerDto.class);
                    itemOwnerDto.setLastBooking(getLastByItemId(item.getId()));
                    itemOwnerDto.setNextBooking(getNextByItemId(item.getId()));
                    itemOwnerDto.setComments(getComments(item.getId()));
                    return itemOwnerDto;
                }).collect(Collectors.toList());
    }


    @Override
    public List<Item> search(String text, int from, int size) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text, OffsetLimitPageable.of(from, size)).stream()
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long itemId, CommentDto commentDto, long userId) throws ItemAvailableException {
        if (!isHasBookingsByItemIdAndUserId(itemId, userId)) {
            throw new ItemAvailableException();
        }
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setItem(modelMapper.map(itemRepository.findById(itemId), Item.class));
        comment.setAuthor(modelMapper.map(userRepository.findById(userId), User.class));
        commentRepository.save(comment);
        CommentDto newCommentDto = modelMapper.map(comment, CommentDto.class);
        newCommentDto.setAuthorName(comment.getAuthor().getName());
        log.info("Added comment with id: {}", newCommentDto.getId());
        return newCommentDto;
    }

    @Override
    public boolean isHasBookingsByItemIdAndUserId(long itemId, long userId) {
        return bookingRepository.countByItemIdAndBookerIdAndStatusAndStartBefore(itemId, userId,
                Status.APPROVED, LocalDateTime.now()) > 0;
    }

    @Override
    public NestedBookingDto getLastByItemId(long itemId) {
        Booking booking = bookingRepository.getFirstByItemIdOrderByStartAsc(itemId);
        if (Objects.isNull(booking)) {
            return null;
        }
        NestedBookingDto nestedBookingDto = modelMapper.map(booking, NestedBookingDto.class);
        nestedBookingDto.setBookerId(booking.getBooker().getId());
        return nestedBookingDto;
    }

    @Override
    public NestedBookingDto getNextByItemId(long itemId) {
        Booking booking = bookingRepository.getFirstByItemIdOrderByEndDesc(itemId);
        if (Objects.isNull(booking)) {
            return null;
        }
        NestedBookingDto nestedBookingDto = modelMapper.map(booking, NestedBookingDto.class);
        nestedBookingDto.setBookerId(booking.getBooker().getId());
        return nestedBookingDto;
    }

    @Override
    public List<CommentDto> getComments(long itemId) {
        return commentRepository.getAllByItemId(itemId).stream()
                .map(comment -> {
                    CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
                    commentDto.setAuthorName(comment.getAuthor().getName());
                    return commentDto;
                }).collect(Collectors.toList());
    }
}