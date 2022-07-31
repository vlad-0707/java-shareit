package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.Identifier;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserValidException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserStorageImpl implements UserStorage{

    private final Identifier identifier;
    private final Map<Long, User> users = new ConcurrentHashMap<>();

    public UserStorageImpl(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public User create(User user) {
        user.setId(identifier.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(long id) throws UserNotFoundException {
        if (Objects.isNull(users.get(id))) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return users.get(id);
    }

    @Override
    public Map<Long, User> getAllUsers() {
        return users;
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public void validEmail(UserDto userDto) throws UserValidException {
        if(getAllUsers().values().stream()
                .anyMatch(u -> u.getEmail().equals(userDto.getEmail()))){
            throw new UserValidException("Эл. адрес уже существует");
        }
    }
}
