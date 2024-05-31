package mconst.rpg.user.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserControllerGetResponse {
    private Integer total;
    private List<UserDTO> items;
}
