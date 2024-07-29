package mconst.rpg.user.services;

import lombok.extern.slf4j.Slf4j;
import mconst.rpg.user.models.UserMapper;
import mconst.rpg.user.models.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
public class UserService {
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;

    public UserService(KeycloakService keycloakService, UserMapper userMapper) {
        this.keycloakService = keycloakService;
        this.userMapper = userMapper;
    }

    public Optional<UserEntity> findById(String id) {
        var userKeycloak = keycloakService.getUserById(id);
        return userKeycloak.map(userKeycloakEntity -> userMapper.map(userKeycloakEntity));
    }
}
