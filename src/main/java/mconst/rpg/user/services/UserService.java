package mconst.rpg.user.services;

import lombok.extern.slf4j.Slf4j;
import mconst.rpg.user.controllers.CustomViolationException;
import mconst.rpg.user.controllers.Violation;
import mconst.rpg.user.models.entities.UserEntity;
import mconst.rpg.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        passwordEncoder = new BCryptPasswordEncoder(10);
    }

    public Page<UserEntity> get(Pageable page) {
        return userRepository.findAll(page);
    }

    @Transactional
    public UserEntity create(UserEntity user) {
        getByUsernameOrEmail(user.getUsername(), user.getEmail())
            .ifPresent(u -> {
                throw new CustomViolationException(
                    new Violation("email", "filed should be unique"),
                    new Violation("username", "filed should be unique")
                );
            });

        user.setId(0L);
        user.setPassword(encodePassword(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public UserEntity replace(UserEntity user) {
        getByUsernameOrEmailAndIdNot(user.getUsername(), user.getEmail(), user.getId())
            .ifPresent(u -> {
                throw new CustomViolationException(
                    new Violation("email", "filed should be unique"),
                    new Violation("username", "filed should be unique")
                );
            });

        user.setPassword(encodePassword(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<UserEntity> getByUsernameOrEmail(String username, String email) {
        var userEntity = this.userRepository.findByUsername(username);
        if (userEntity == null) {
            return Optional.ofNullable(this.userRepository.findByEmail(email));
        }
        return Optional.of(userEntity);
    }

    public Optional<UserEntity> getByUsernameOrEmailAndIdNot(String username, String email, Long id) {
        var userEntity = this.userRepository.findByUsernameAndIdNot(username, id);
        if (userEntity == null) {
            return Optional.ofNullable(this.userRepository.findByEmailAndIdNot(email, id));
        }
        return Optional.of(userEntity);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
