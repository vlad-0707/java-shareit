package shareit.user.service;


import shareit.exception.UserNotFoundException;
import shareit.exception.UserValidException;
import shareit.user.dto.UserDto;
import shareit.user.model.User;

import java.util.List;

public interface UserService {
    User create(UserDto userDto) throws UserValidException, UserNotFoundException;
    User getById(long id) throws UserNotFoundException;
    List<User> getAllUsers();
    User update(UserDto userDto, Long userId) throws UserValidException, UserNotFoundException;
    void delete(long id);
}
