package mconst.rpg.user.models.mappers;

import mconst.rpg.user.models.dtos.UserDto;
import mconst.rpg.user.models.dtos.UserOptionalDto;
import mconst.rpg.user.models.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto map(UserEntity user);
    UserEntity map(UserDto user);
    UserEntity mapOptional(UserOptionalDto user);
    UserOptionalDto mapOptional(UserEntity user);
}
