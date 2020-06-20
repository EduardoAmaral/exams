package com.eamaral.exams.user.application.controller;

import com.eamaral.exams.configuration.controller.ControllerIntegrationTest;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerIntegrationTest {

    @Test
    public void auth_shouldReturnAuthenticatedFalse_whenUserIsNotLogged() throws Exception {
        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isForbidden());
    }
}