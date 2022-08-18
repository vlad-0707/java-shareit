package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserValidException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User create(UserDto userDto) throws UserValidException, UserNotFoundException;
    User getById(long id) throws UserNotFoundException;
    List<User> getAllUsers();
    User update(UserDto userDto, Long userId) throws UserValidException, UserNotFoundException;
    void delete(long id);
}
