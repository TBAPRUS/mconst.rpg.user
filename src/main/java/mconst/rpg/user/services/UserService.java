package mconst.rpg.user.services;

import lombok.extern.slf4j.Slf4j;
import mconst.rpg.user.controllers.CustomViolationException;
import mconst.rpg.user.controllers.Violation;
import mconst.rpg.user.models.dtos.UserDto;
import mconst.rpg.user.models.dtos.UserServiceGetResponse;
import mconst.rpg.user.models.mappers.UserMapper;
import mconst.rpg.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = UserMapper.INSTANCE;
    }

    public UserServiceGetResponse get(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        var users = userRepository.findAll(pageable);

        List<UserDto> userDTOS = users.stream()
                .map(userMapper::map)
                .toList();
        Integer total = Math.toIntExact(users.getTotalElements());

        return new UserServiceGetResponse(total, userDTOS);
    }

    public UserDto getByUsernameOrEmail(String username, String email) {
        var userEntity = this.userRepository.findByUsernameOrEmail(username, email);
        return userEntity == null ? null : userMapper.map(userEntity);
    }

    public UserDto create(UserDto user) {
        var existsUser = getByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (existsUser != null) {
            var violations = new ArrayList<Violation>();
            violations.add(new Violation("email", "filed should be unique"));
            violations.add(new Violation("username", "filed should be unique"));
            throw new CustomViolationException("", violations);
        }
        var encoder = new BCryptPasswordEncoder(10);

        var userEntity = userMapper.map(user);
        userEntity.setPassword(encoder.encode(user.getPassword()));
        var createdUser = userRepository.save(userEntity);

        return userMapper.map(createdUser);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto findById(Long id) {
        var userEntity = userRepository.findById(id);
        return userEntity.map(userMapper::map).orElse(null);
    }
}
