package mconst.rpg.user.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mconst.rpg.user.models.dtos.UserDto;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long total;
    private List<UserDto> items;
}
