package com.eamaral.worker.question.application.scheduler;

import com.eamaral.worker.configuration.scheduler.SchedulerIntegrationTest;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

class QuestionSchedulerTest extends SchedulerIntegrationTest {

    @Test
    @DisplayName("should run at least once after two seconds")
    void upsertQuestions() {
        Awaitility.await()
                .atMost(Durations.ONE_SECOND)
                .untilAsserted(() -> {
                    verify(questionScheduler).upsertQuestions();
                    verify(questionService).upsert();
                });
    }
}