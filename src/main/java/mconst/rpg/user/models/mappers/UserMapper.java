package mconst.rpg.user.models.mappers;

import mconst.rpg.user.models.dtos.UserDto;
import mconst.rpg.user.models.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto map(UserEntity user);
    UserEntity map(UserDto user);
}
