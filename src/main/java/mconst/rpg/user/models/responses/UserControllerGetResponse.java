package mconst.rpg.user.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mconst.rpg.user.models.dtos.UserDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserControllerGetResponse {
    private Long total;
    private List<UserDto> items;
}
