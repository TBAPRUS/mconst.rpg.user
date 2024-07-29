package mconst.rpg.user.models;

import mconst.rpg.user.models.dtos.UserKeycloakDto;
import mconst.rpg.user.models.entities.UserKeycloakEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserKeycloakMapper {
    UserKeycloakDto map(UserKeycloakEntity user);
    UserKeycloakEntity map(UserKeycloakDto user);

}
