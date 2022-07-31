package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserValidException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserStorage userStorage;

    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) throws UserValidException, UserNotFoundException {
        userStorage.validEmail(userDto);
        User user = userStorage.create(userMapper.toUser(userDto));
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto getById(long id) throws UserNotFoundException {
        return userMapper.toUserDto(userStorage.getById(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().values().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) throws UserNotFoundException, UserValidException {
        getById(userId);
        userStorage.validEmail(userDto);
        userDto.setId(userId);
        User user = userMapper.toUser(userDto);
        userStorage.update(user);
        return userMapper.toUserDto(user);
    }

    @Override
    public void delete(long id) {
        userStorage.delete(id);
    }
}
