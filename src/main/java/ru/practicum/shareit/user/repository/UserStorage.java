package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserValidException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

public interface UserStorage {
    User create(User user);
    User getById(long id) throws UserNotFoundException;
    Map<Long, User> getAllUsers();
    void update(User user);
    void delete(long id);
    void validEmail(UserDto userDto) throws UserValidException;
}
