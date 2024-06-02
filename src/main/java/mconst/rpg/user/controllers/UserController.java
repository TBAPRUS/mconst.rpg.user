package mconst.rpg.user.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import mconst.rpg.user.models.dtos.UserControllerGetResponse;
import mconst.rpg.user.models.dtos.UserDto;
import mconst.rpg.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public UserControllerGetResponse get(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "20") @Min(1) @Max(1000) Integer pageSize
    ) {
        var response = userService.get(pageNumber, pageSize);
        return new UserControllerGetResponse(response.getTotal(), response.getItems());
    }

    @PostMapping()
    public UserDto post(@Valid @RequestBody UserDto user) {
        return userService.create(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        var user = userService.findById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userService.deleteById(id);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        var user = userService.findById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return  user;
    }
}
