package mconst.rpg.user.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import mconst.rpg.user.annotations.Log4Method;
import mconst.rpg.user.models.responses.UserResponse;
import mconst.rpg.user.models.dtos.UserDto;
import mconst.rpg.user.models.dtos.UserOptionalDto;
import mconst.rpg.user.models.UserMapper;
import mconst.rpg.user.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Log4Method()
    @GetMapping()
    public UserResponse get(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "20") @Min(1) @Max(1000) Integer pageSize
    ) {
        Pageable pagination = PageRequest.of(pageNumber, pageSize);
        var page = userService.get(pagination);

        return new UserResponse(
            page.getTotalElements(),
            userMapper.map(page)
        );
    }

    @PostMapping()
    public UserDto post(@Valid @RequestBody UserDto user) {
        var userEntity = userMapper.map(user);
        var createdUser = userService.create(userEntity);
        return userMapper.map(createdUser);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        userService.deleteById(id);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        var userEntity = userService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return userMapper.map(userEntity);
    }

    @PutMapping("/{id}")
    public UserDto put(@Valid @RequestBody UserDto user, @PathVariable Long id) {
        userService
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setId(id);
        var userEntity = userService.replace(userMapper.map(user));
        return userMapper.map(userEntity);
    }

    @PatchMapping("/{id}")
    public UserDto patch(@Valid @RequestBody UserOptionalDto user, @PathVariable Long id) {
        var existedUser = userService
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setId(id);
        if (user.getUsername() == null) {
            user.setUsername(existedUser.getUsername());
        }
        if (user.getEmail() == null) {
            user.setEmail(existedUser.getEmail());
        }
        if (user.getPassword() == null) {
            user.setPassword(existedUser.getPassword());
        }
        var userEntity = userService.replace(userMapper.mapOptional(user));
        return userMapper.map(userEntity);
    }
}
