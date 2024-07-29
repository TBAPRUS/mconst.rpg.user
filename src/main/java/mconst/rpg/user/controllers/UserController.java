package mconst.rpg.user.controllers;

import lombok.extern.slf4j.Slf4j;
import mconst.rpg.user.models.dtos.UserDto;
import mconst.rpg.user.models.UserMapper;
import mconst.rpg.user.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable String id) {
        var userEntity = userService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return userMapper.map(userEntity);
    }
}
