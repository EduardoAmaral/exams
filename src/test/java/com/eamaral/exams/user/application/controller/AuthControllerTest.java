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
    public void auth_shouldReturnAuthenticatedTrue_whenUserNameIsEqualToId() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn("12345");

        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    public void auth_shouldReturnAuthenticatedFalse_whenUserNameIsAnonymousUser() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn("anonymousUser");

        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    public void auth_shouldReturnAuthenticatedFalse_whenUserNameIsEmpty() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn("");

        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    public void auth_shouldReturnAuthenticatedFalse_whenUserNameIsNull() throws Exception {
        when(userPort.getCurrentUserId()).thenReturn(null);

        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }
}