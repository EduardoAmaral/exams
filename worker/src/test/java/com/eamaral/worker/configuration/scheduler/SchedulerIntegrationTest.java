package com.eamaral.worker.configuration.scheduler;

import com.eamaral.worker.question.application.scheduler.QuestionScheduler;
import com.eamaral.worker.question.domain.service.QuestionService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SchedulerIntegrationTestConfiguration.class})
public abstract class SchedulerIntegrationTest {

    @SpyBean
    protected QuestionScheduler questionScheduler;

    @MockBean
    protected QuestionService questionService;
}
