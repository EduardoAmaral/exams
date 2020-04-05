package com.eamaral.exams.user.application.controller;

import com.eamaral.exams.user.domain.port.UserPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final UserPort userPort;

    private final String[] unauthenticatedUsers;

    public AuthController(UserPort userPort, @Value("${unauthorized.users}") String[] unauthenticatedUsers) {
        this.userPort = userPort;
        this.unauthenticatedUsers = unauthenticatedUsers;
    }


    @GetMapping
    public ResponseEntity<Boolean> auth() {
        String userId = userPort.getCurrentUserId();
        boolean authenticated = userId != null && !Arrays.asList(unauthenticatedUsers).contains(userId);

        return ok(authenticated);
    }

}
