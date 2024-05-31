package mconst.rpg.user.controllers;

import lombok.extern.slf4j.Slf4j;
import mconst.rpg.user.models.dtos.UserControllerGetResponse;
import mconst.rpg.user.models.dtos.UserDTO;
import mconst.rpg.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public UserControllerGetResponse get(@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "20") Integer pageSize) {
        var response = userService.get(pageNumber, pageSize);
//        return new UserControllerGetResponse(response.getTotal(), response.getItems());
        return null;
    }
}
