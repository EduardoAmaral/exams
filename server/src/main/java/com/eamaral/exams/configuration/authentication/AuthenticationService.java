package com.eamaral.exams.configuration.authentication;

import com.eamaral.exams.user.domain.User;
import com.eamaral.exams.user.domain.port.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthenticationService extends OidcUserService {

    private final UserRepositoryPort userRepositoryPort;

    public AuthenticationService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        final OidcUser user = super.loadUser(userRequest);

        return retrieveUserDetails(user);
    }

    private OidcUser retrieveUserDetails(OidcUser oidcUser) {
        DefaultOidcUser user = (DefaultOidcUser) oidcUser;
        log.info("Getting user details for {}", user.getEmail());

        userRepositoryPort.findByEmail(user.getEmail()).or(() -> {
            log.info("Creating new user for {}", user.getEmail());
            User newUser = User.builder()
                    .id(user.getSubject())
                    .email(user.getEmail())
                    .name(user.getGivenName())
                    .surname(user.getFamilyName())
                    .picture(user.getPicture())
                    .build();

            return Optional.of(userRepositoryPort.save(newUser));
        });

        return oidcUser;
    }
}
