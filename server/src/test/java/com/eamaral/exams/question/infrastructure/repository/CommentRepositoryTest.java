package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.CommentEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CommentRepositoryTest extends JpaIntegrationTest {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void create_whenFieldsAreValid_shouldReturnAQuestionWithId() {
        Comment comment = getComment(1L);

        Comment result = repository.create(comment);

        assertThat(result.getId()).isNotZero();
    }

    @Test
    public void create_whenRequiredFieldsAreNotFilled_shouldThrowException() {
        Comment comment = CommentEntity.builder().build();

        List<String> validationMessages = List.of("Message is required",
                "Question is required",
                "Author is required");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.create(comment))
                .matches(e -> e.getConstraintViolations().size() == validationMessages.size())
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    public void create_whenMessageIsGreaterThanExpected_shouldThrowException() {
        Comment comment = CommentEntity.builder()
                .message(new String(new char[301]).replace('\0', 'A'))
                .author("1234")
                .questionId(1L)
                .creationDate(ZonedDateTime.now())
                .build();

        List<String> validationMessages = List.of("Comments cannot have more than 300 characters");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.create(comment))
                .matches(e -> e.getConstraintViolations().size() == validationMessages.size())
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    public void findAllBy_shouldReturnAllQuestionComments() {
        long questionId = 1L;
        entityManager.merge(getComment(questionId));
        entityManager.merge(getComment(questionId));
        entityManager.merge(getComment(2L));

        List<Comment> result = repository.findAllBy(questionId);

        assertThat(result).hasSize(2);
    }

    private CommentEntity getComment(long questionId) {
        return CommentEntity.builder()
                .message("Comment")
                .author("1234")
                .questionId(questionId)
                .creationDate(ZonedDateTime.now())
                .build();
    }
}