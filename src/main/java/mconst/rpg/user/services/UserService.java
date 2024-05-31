package mconst.rpg.user.services;

import lombok.extern.slf4j.Slf4j;
import mconst.rpg.user.models.dtos.UserDTO;
import mconst.rpg.user.models.dtos.UserServiceGetResponse;
import mconst.rpg.user.models.entities.UserEntity;
import mconst.rpg.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserServiceGetResponse get(Integer pageNumber, Integer pageSize) {
        log.info("pageNumber: {}", pageNumber);
        log.info("pageSize: {}", pageSize);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        var users = userRepository.findAll(pageable);

        log.info(users.toString());

        List<UserDTO> userDTOS = new ArrayList<>(); //users.stream().map()
        Integer total = Math.toIntExact(users.getTotalElements());
//
        var response = new UserServiceGetResponse(total, userDTOS);

        return response;
    }
}
