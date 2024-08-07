package mconst.rpg.user.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class UserDto {
    private String id;

    @NotEmpty
    @Size(min = 4, max = 36)
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 8, max = 256)
    private String password;
}
