package com.eamaral.exams.configuration.controller;

import com.eamaral.exams.exam.domain.port.ExamPort;
import com.eamaral.exams.question.domain.port.CommentPort;
import com.eamaral.exams.question.domain.port.QuestionPort;
import com.eamaral.exams.question.domain.port.SubjectPort;
import com.eamaral.exams.user.domain.port.UserPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest
@ContextConfiguration(classes = {ControllerTestConfiguration.class})
@WithMockUser
public abstract class ControllerIntegrationTest {

    @MockBean
    protected QuestionPort questionPort;

    @MockBean
    protected SubjectPort subjectPort;

    @MockBean
    protected UserPort userPort;

    @MockBean
    protected ExamPort examPort;

    @MockBean
    protected CommentPort commentPort;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
