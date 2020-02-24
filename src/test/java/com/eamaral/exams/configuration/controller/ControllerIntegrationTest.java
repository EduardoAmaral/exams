package com.eamaral.exams.configuration.controller;

import com.eamaral.exams.question.domain.services.QuestionService;
import com.eamaral.exams.question.domain.services.SubjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest
@ContextConfiguration(classes = {ControllerTestConfiguration.class})
public abstract class ControllerIntegrationTest {

    @MockBean
    protected QuestionService questionService;

    @MockBean
    protected SubjectService subjectService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
