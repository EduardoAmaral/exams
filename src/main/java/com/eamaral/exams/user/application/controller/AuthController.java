package com.eamaral.exams.user.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Value("${unauthorized.users}")
    private String[] unauthenticatedUsers;

    @GetMapping
    public ResponseEntity<?> auth(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean authenticated = !Arrays.asList(unauthenticatedUsers).contains(userId);

        return ok(authenticated);
    }

}
