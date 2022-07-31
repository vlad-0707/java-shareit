package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserValidException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryUserStorage implements UserStorage{
    private static long id;
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    @Override
    public User create(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(long id) throws UserNotFoundException {
        if (Objects.isNull(users.get(id))) {
            throw new UserNotFoundException("User not found");
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
            throw new UserValidException("email already exists");
        }
    }
}
