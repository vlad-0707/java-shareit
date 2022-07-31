package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.UserException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserValidException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) throws UserValidException, UserNotFoundException, UserException {
        return userService.create(userDto);
    }

    @GetMapping("{id}")
    public UserDto getUserById(@PathVariable long id) throws UserNotFoundException {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable Long userId) throws UserNotFoundException, UserValidException {
        return userService.update(userDto, userId);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id){
        userService.delete(id);
    }
}
