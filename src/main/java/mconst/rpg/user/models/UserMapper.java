package mconst.rpg.user.models;

import mconst.rpg.user.models.dtos.UserDto;
import mconst.rpg.user.models.dtos.UserKeycloakDto;
import mconst.rpg.user.models.entities.UserEntity;
import mconst.rpg.user.models.entities.UserKeycloakEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserDto map(UserEntity user);
    UserEntity map(UserDto user);
    UserEntity map(UserKeycloakEntity user);
    List<UserDto> map(Iterable<UserEntity> users);
}
