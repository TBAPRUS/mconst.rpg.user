package mconst.rpg.user.services;

import mconst.rpg.user.models.UserKeycloakMapper;
import mconst.rpg.user.models.UserMapper;
import mconst.rpg.user.models.dtos.UserKeycloakDto;
import mconst.rpg.user.models.entities.UserKeycloakEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class KeycloakService {
    private KeycloakTokenService keycloakTokenService;
    private UserKeycloakMapper userKeycloakMapper;
    private String keycloakServer;
    private String realm;

    public KeycloakService(KeycloakTokenService keycloakTokenService, UserKeycloakMapper userKeycloakMapper, String keycloakServer, String realm) {
        this.keycloakTokenService = keycloakTokenService;
        this.userKeycloakMapper = userKeycloakMapper;
        this.keycloakServer = keycloakServer;
        this.realm = realm;
    }

    private String adminUser() {
        return keycloakServer + "/admin/realms/" + realm;
    }

    private String adminUrl(String path) {
         return adminUser() + path;
    }

    public Optional<UserKeycloakEntity> getUserById(String id) {
        var token = keycloakTokenService.getAccessToken().orElseThrow(() -> new RuntimeException("No keycloak token"));
        var restTemplate = new RestTemplate();

        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        var entity = new HttpEntity<>(headers);

        try {
            var data = restTemplate.exchange(adminUrl("/users/" + id), HttpMethod.GET, entity, UserKeycloakDto.class);
            var body = data.getBody();
            return Optional.of(userKeycloakMapper.map(body));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }
}
