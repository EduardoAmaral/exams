package com.eamaral.exams.user.application.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @GetMapping
    public ResponseEntity<?> auth() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Authenticating {}", principal);
        if (principal instanceof DefaultOidcUser) {
            var user = (DefaultOidcUser) principal;
            return ok(new User(
                    user.getSubject(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPicture())
            );
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @AllArgsConstructor
    @Getter
    public static class User {
        private final String id;
        private final String name;
        private final String email;
        private final String profileSrc;
    }
}
