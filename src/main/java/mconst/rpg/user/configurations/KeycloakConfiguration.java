package mconst.rpg.user.configurations;

import mconst.rpg.user.models.UserKeycloakMapper;
import mconst.rpg.user.services.KeycloakService;
import mconst.rpg.user.services.KeycloakTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class KeycloakConfiguration {
    private final Environment environment;
    private final UserKeycloakMapper userKeycloakMapper;

    public KeycloakConfiguration(Environment environment, UserKeycloakMapper userKeycloakMapper) {
        this.environment = environment;
        this.userKeycloakMapper = userKeycloakMapper;
    }

    @Bean
    public KeycloakTokenService keycloakTokenService() {
        return new KeycloakTokenService(
                environment.getProperty("custom.keycloak.server") + "/realms/" + environment.getProperty("custom.keycloak.realm"),
                environment.getProperty("custom.keycloak.client-id"),
                environment.getProperty("custom.keycloak.username"),
                environment.getProperty("custom.keycloak.password")
        );
    }

    @Bean
    public KeycloakService keycloakService() {
        return new KeycloakService(keycloakTokenService(), userKeycloakMapper, environment.getProperty("custom.keycloak.server"), environment.getProperty("custom.keycloak.realm"));
    }
}