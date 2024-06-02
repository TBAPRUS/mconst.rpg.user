package mconst.rpg.user.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class UserServiceGetResponse {
    private Integer total;
    private List<UserDto> items;
}
