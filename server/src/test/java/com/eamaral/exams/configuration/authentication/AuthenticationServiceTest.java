package com.eamaral.exams.configuration.authentication;

import com.eamaral.exams.user.domain.User;
import com.eamaral.exams.user.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private AuthenticationService service;

    @Test
    @DisplayName("should save a new user when it doesn't exist")
    void saveNewUser() {
        OidcUserRequest request = new OidcUserRequest(
                ClientRegistration.withRegistrationId("test")
                        .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                        .clientId("test-client")
                        .tokenUri("https://token-uri.example.org")
                        .build(),
                new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                        "https://token-uri.example.org",
                        Instant.now(),
                        Instant.now().plusSeconds(10000)),
                new OidcIdToken("https://token-uri.example.org",
                        Instant.now(),
                        Instant.now().plusSeconds(10000),
                        Map.of("email", "a@email.com", "sub", "1")));

        service.loadUser(request);

        verify(userRepositoryPort).save(any());
    }

    @Test
    @DisplayName("should not create a new user when it already exists")
    void login_withAnExistentUser() {
        OidcUserRequest request = new OidcUserRequest(
                ClientRegistration.withRegistrationId("test")
                        .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                        .clientId("test-client")
                        .tokenUri("https://token-uri.example.org")
                        .build(),
                new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                        "https://token-uri.example.org",
                        Instant.now(),
                        Instant.now().plusSeconds(10000)),
                new OidcIdToken("https://token-uri.example.org",
                        Instant.now(),
                        Instant.now().plusSeconds(10000),
                        Map.of("email", "a@email.com", "sub", "1")));

        when(userRepositoryPort.findByEmail("a@email.com"))
                .thenReturn(Optional.of(User.builder().build()));

        service.loadUser(request);

        verify(userRepositoryPort, never()).save(any());
    }
}