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
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = UserMapper.INSTANCE;
        passwordEncoder = new BCryptPasswordEncoder(10);
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
        var userEntity = this.userRepository.findByUsername(username);
        if (userEntity == null) {
            userEntity = this.userRepository.findByEmail(email);
        }
        return userEntity == null ? null : userMapper.map(userEntity);
    }

    public UserDto getByUsernameOrEmailAndIdNot(String username, String email, Long id) {
        var userEntity = this.userRepository.findByUsernameAndIdNot(username, id);
        if (userEntity == null) {
            userEntity = this.userRepository.findByEmailAndIdNot(email, id);
        }
        return userEntity == null ? null : userMapper.map(userEntity);
    }

    public UserDto create(UserDto user) {
        checkUnique(user.getUsername(), user.getEmail());

        var userEntity = userMapper.map(user);
        userEntity.setPassword(encodePassword(user.getPassword()));
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

    public UserDto replace(UserDto user) {
        checkUnique(user.getUsername(), user.getEmail(), user.getId());

        var userEntity = userMapper.map(user);
        userEntity.setPassword(encodePassword(userEntity.getPassword()));
        var replacedUser = userRepository.save(userEntity);
        return userMapper.map(replacedUser);
    }

    private void checkUnique(String username, String email) {
        var existsUser = getByUsernameOrEmail(username, email);
        if (existsUser != null) {
            throwUniqueViolations();
        }
    }

    private void checkUnique(String username, String email, Long id) {
        var existsUser = getByUsernameOrEmailAndIdNot(username, email, id);
        if (existsUser != null) {
            throwUniqueViolations();
        }
    }

    private void throwUniqueViolations() {
        var violations = new ArrayList<Violation>();
        violations.add(new Violation("email", "filed should be unique"));
        violations.add(new Violation("username", "filed should be unique"));
        throw new CustomViolationException("", violations);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
