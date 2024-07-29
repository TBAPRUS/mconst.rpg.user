package mconst.rpg.user.models.entities;

import lombok.*;

@Data
public class UserEntity {
    private String id;
    private String username;
    private String email;
    private String password;
}
