package com.eamaral.worker.question.domain.service;

import com.eamaral.worker.question.domain.Question;
import com.eamaral.worker.question.domain.port.QuestionDocumentPort;
import com.eamaral.worker.question.domain.port.QuestionPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.eamaral.worker.question.domain.QuestionType.MULTIPLE_CHOICES;
import static com.eamaral.worker.question.domain.QuestionType.TRUE_OR_FALSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionPort questionPort;

    @Mock
    private QuestionDocumentPort questionDocumentPort;

    @InjectMocks
    private QuestionService questionService;

    @Captor
    private ArgumentCaptor<List<Question>> questionsCaptor;

    @Test
    @DisplayName("should upsert questions")
    void updateQuestions() {
        final Question questionOne = Question.builder()
                .id(1L)
                .keywords("Key 1")
                .statement("Statement 1")
                .subject("Subject")
                .type(TRUE_OR_FALSE)
                .author("Author 1")
                .build();
        final Question questionTwo = Question.builder()
                .id(2L)
                .keywords("Key 2")
                .statement("Statement 2")
                .subject("Subject")
                .type(MULTIPLE_CHOICES)
                .author("Author 2")
                .build();
        List<Question> questions = List.of(
                questionOne,
                questionTwo
        );

        when(questionPort.findAll()).thenReturn(questions);

        final long updatedRecords = questionService.upsert();

        verify(questionDocumentPort).deleteAll();
        verify(questionDocumentPort).save(questionsCaptor.capture());

        assertThat(updatedRecords).isEqualTo(2);

        assertThat(questionsCaptor.getValue())
                .extracting(Question::getId,
                        Question::getStatement,
                        Question::getType,
                        Question::getSubject,
                        Question::getAuthor)
                .containsExactlyInAnyOrder(
                        tuple(1L, "Statement 1", TRUE_OR_FALSE, "Subject", "Author 1"),
                        tuple(2L, "Statement 2", MULTIPLE_CHOICES, "Subject", "Author 2")
                );
    }

}