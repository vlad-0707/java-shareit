package shareit.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import shareit.exception.UserNotFoundException;
import shareit.exception.UserValidException;
import shareit.user.dto.UserDto;
import shareit.user.service.UserService;


import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final ModelMapper modelMapper;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) throws UserValidException, UserNotFoundException {
        return modelMapper.map(userService.create(userDto), UserDto.class);
    }

    @GetMapping("{id}")
    public UserDto getUserById(@PathVariable long id) throws UserNotFoundException {
        return modelMapper.map(userService.getById(id), UserDto.class);
    }

    @GetMapping
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto,
                       @PathVariable long userId) throws UserNotFoundException, UserValidException {
        return modelMapper.map(userService.update(userDto, userId), UserDto.class);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id){
        userService.delete(id);
    }
}
