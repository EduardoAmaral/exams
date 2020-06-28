package com.eamaral.exams.user.domain.service;

import com.eamaral.exams.user.domain.port.UserPort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserPort {

    public String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
