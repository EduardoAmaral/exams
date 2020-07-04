package com.eamaral.exams.user.domain.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@WithMockUser
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Test
    void getCurrentUserId_shouldReturnTheUserIdOfLoggedUser() {
        assertThat(service.getCurrentUserId()).isNotBlank();
    }
}