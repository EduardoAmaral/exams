package com.eamaral.exams.user.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerIntegrationTest {

    @Test
    @DisplayName("should return forbidden if user is not logged")
    void auth_shouldReturnAuthenticatedFalse_whenUserIsNotLogged() throws Exception {
        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isForbidden());
    }
}