package com.amaral.exams.configuration.controller;

import com.amaral.exams.question.domain.services.QuestionService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest
@ContextConfiguration(classes = {ControllerTestConfiguration.class})
public abstract class ControllerIntegrationTest {

    @MockBean
    protected QuestionService questionService;

}
