package mconst.rpg.user.models.entities;

import lombok.Data;

@Data
public class UserKeycloakEntity {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean emailVerified;
    private Boolean enabled;
    private Boolean totp;
}
