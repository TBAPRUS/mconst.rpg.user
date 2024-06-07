package mconst.rpg.user.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class UserOptionalDto {
    private Long id;

    @Size(min = 4, max = 36)
    private String username;

    @Email
    private String email;

    @Size(min = 8, max = 256)
    private String password;
}
