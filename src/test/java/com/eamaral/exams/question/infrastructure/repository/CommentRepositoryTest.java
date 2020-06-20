package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.CommentEntity;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.MultipleChoiceEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CommentRepositoryTest extends JpaIntegrationTest {

    @Autowired
    private CommentRepository repository;

    @Test
    public void save_whenFieldsAreValid_shouldReturnAQuestionWithId() {
        Comment comment = CommentEntity.builder()
                .message("Comment")
                .author("1234")
                .questionId(1L)
                .build();

        Comment result = repository.create(comment);

        assertThat(result.getId()).isNotZero();
    }

    @Test
    public void save_whenRequiredFieldsAreNotFilled_shouldThrowException() {
        Comment comment = CommentEntity.builder().build();

        List<String> validationMessages = List.of("Comment's message is required",
                "Comment's question is required",
                "Comment's author is required");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.create(comment))
                .matches(e -> e.getConstraintViolations().size() == validationMessages.size())
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    public void save_whenMessageIsGreaterThanExpected_shouldThrowException() {
        Comment comment = CommentEntity.builder()
                .message(new String(new char[301]).replace('\0', 'A'))
                .author("1234")
                .questionId(1L)
                .build();

        List<String> validationMessages = List.of("Comments cannot have more than 300 characters");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.create(comment))
                .matches(e -> e.getConstraintViolations().size() == validationMessages.size())
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }
}