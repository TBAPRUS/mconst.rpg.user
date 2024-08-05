package mconst.rpg.user.services;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class KeycloakTokenService {
    private final String keycloakUri;
    private final String clientId;
    private final String username;
    private final String password;
    private TokensResponse tokens;
    private Thread thread;

    public KeycloakTokenService(String keycloakUri, String clientId, String username, String password) {
        this.keycloakUri = keycloakUri;
        this.clientId = clientId;
        this.username = username;
        this.password = password;
    }

    @PostConstruct
    public void init() {
        tokens = getTokens();

        thread = new Thread(new RefreshToken());
        thread.start();
    }

    @PreDestroy
    public void close() {
        thread.interrupt();
    }

    public Optional<String> getAccessToken() {
        if (tokens == null) {
            return Optional.empty();
        }
        return Optional.of(tokens.accessToken);
    }

    private TokensResponse getTokens() {
        var restTemplate = new RestTemplate();

        var payload = new TokensFromCredentialsRequest("password", clientId, username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var entity = new HttpEntity<>(payload.toFormUrlencoded(), headers);
        var response = restTemplate.exchange(keycloakUri + "/protocol/openid-connect/token", HttpMethod.POST, entity, TokensResponse.class);
        var body = response.getBody();

        if (body == null) {
            throw new RuntimeException("Empty response from Keycloak when getting tokens from credentials");
        }

        return body;
    }

    private class RefreshToken implements Runnable {
        private void refresh() {
            var restTemplate = new RestTemplate();

            var payload = new TokensFromRefreshRequest("refresh_token", clientId, tokens.getRefreshToken());
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            var entity = new HttpEntity<>(payload.toFormUrlencoded(), headers);
            try {
                var response = restTemplate.exchange(keycloakUri + "/protocol/openid-connect/token", HttpMethod.POST, entity, TokensResponse.class);
                var body = response.getBody();

                if (body == null) {
                    throw new RuntimeException("Empty response from Keycloak when getting tokens from refresh token");
                }

                tokens = body;
            } catch (Exception exception) {
                throw exception;
            }
        }

        public void run() {
            while (true) {
                try {
                    var timeToSleep = Math.min(tokens.getRefreshExpiresIn(), tokens.getExpiresIn()) - 10;

                    TimeUnit.SECONDS.sleep(timeToSleep);
                    refresh();
                } catch (InterruptedException exception) {
                    log.error(exception.getMessage());
                    return;
                }
            }
        }
    }

    @AllArgsConstructor
    private class TokensFromCredentialsRequest {
        private String grantType;
        private String clientId;
        private String username;
        private String password;

        public MultiValueMap<String, String> toFormUrlencoded() {
            var map = new LinkedMultiValueMap<String, String>();

            map.add("grant_type", grantType);
            map.add("client_id", clientId);
            map.add("username", username);
            map.add("password", password);

            return map;
        }
    }

    @AllArgsConstructor
    private class TokensFromRefreshRequest {
        private String grantType;
        private String clientId;
        private String refreshToken;

        public MultiValueMap<String, String> toFormUrlencoded() {
            var map = new LinkedMultiValueMap<String, String>();

            map.add("grant_type", grantType);
            map.add("client_id", clientId);
            map.add("refresh_token", refreshToken);

            return map;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class TokensResponse {
        private String accessToken;
        private Long expiresIn;
        private Long refreshExpiresIn;
        private String refreshToken;
        private String tokenType;
        private String sessionState;
        private String scope;
    }
}
