package com.eamaral.worker.question.application.scheduler;

import com.eamaral.worker.question.domain.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class QuestionScheduler {

    private final QuestionService questionService;

    @Scheduled(cron = "${update.questions.interval}")
    public void upsertQuestions() {
        log.info("Upsert questions");

        final long recordsUpsert = questionService.upsert();

        log.info("Finished questions upsert with {} documents updated", recordsUpsert);
    }
}
