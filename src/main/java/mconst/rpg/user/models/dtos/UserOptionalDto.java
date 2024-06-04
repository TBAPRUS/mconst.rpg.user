package mconst.rpg.user.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOptionalDto {
    private Long id;

    @Size(min = 4, max = 36)
    private String username;

    @Email
    private String email;

    @Size(min = 8, max = 256)
    private String password;
}
