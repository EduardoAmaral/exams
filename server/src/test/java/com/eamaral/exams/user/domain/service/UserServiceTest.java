package com.eamaral.exams.user.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WithMockUser
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Test
    public void getCurrentUserId_shouldReturnTheUserIdOfLoggedUser() {
        assertThat(service.getCurrentUserId()).isNotBlank();
    }
}