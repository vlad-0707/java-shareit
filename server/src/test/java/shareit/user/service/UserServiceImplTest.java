package shareit.user.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import shareit.exception.UserNotFoundException;
import shareit.user.dto.UserDto;
import shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ModelMapper modelMapper;


    @Test
    void create(){
        User user = new User(1L, "Ivan", "Ivanov@yandex.ru");
        User newUser = userService.create(modelMapper.map(user, UserDto.class));
        Assertions.assertEquals(user.getName(), newUser.getName());

    }

    @Test
    void getById() throws UserNotFoundException {
        User user = new User(1L, "Ivan", "Ivanov@yandex.ru");
        User newUser = userService.create(modelMapper.map(user, UserDto.class));
        Assertions.assertEquals(user.getName(), userService.getById(user.getId()).getName());

    }

    @Test
    void getAllUsers() {
        User firstUser = new User(1L, "Ivan", "Ivanov@yandex.ru");
        User secondUser = new User(2L, "Egor", "Egorov@yandex.ru");
        List<User> users = new ArrayList<>() {{
            add(firstUser);
            add(secondUser);
        }};
        userService.create(modelMapper.map(firstUser, UserDto.class));
        userService.create(modelMapper.map(secondUser, UserDto.class));

        Assertions.assertEquals(users, userService.getAllUsers());

    }

    @Test
    void update() throws UserNotFoundException {

        User user = new User(1L, "Ivan", "Ivanov@yandex.ru");
        User updateUser = new User(1L, "Egor", "Egorov@yandex.ru");

        userService.create(modelMapper.map(user, UserDto.class));
        User newUser = userService.update(modelMapper.map(updateUser, UserDto.class), updateUser.getId());

        Assertions.assertEquals(updateUser.getName(), newUser.getName());


    }

    @Test
    void delete() {
        User user = new User(1L, "Ivan", "Ivanov@yandex.ru");
        userService.create(modelMapper.map(user, UserDto.class));
        userService.delete(user.getId());
        assertThrows(UserNotFoundException.class, () -> userService.getById(user.getId()));
    }
}

