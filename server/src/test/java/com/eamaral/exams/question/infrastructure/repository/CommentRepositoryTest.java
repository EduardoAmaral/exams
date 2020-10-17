package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.question.domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.List;

import static com.eamaral.exams.question.infrastructure.repository.jpa.entity.CommentEntity.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CommentRepositoryTest extends JpaIntegrationTest {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("should create a comment")
    void create_whenFieldsAreValid_shouldReturnAQuestionWithId() {
        Comment comment = getComment(1L);

        Comment result = repository.create(comment);

        assertThat(result.getId()).isNotZero();
    }

    @Test
    @DisplayName("should validate required fields when creating a comment")
    void create_whenRequiredFieldsAreNotFilled_shouldThrowException() {
        List<String> validationMessages = List.of("Message is required",
                "Question is required",
                "Author is required");

        final Comment emptyComment = Comment.builder().build();
        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.create(emptyComment))
                .matches(e -> e.getConstraintViolations().size() == validationMessages.size())
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    @DisplayName("should validate the message length when creating a comment")
    void create_whenMessageIsGreaterThanExpected_shouldThrowException() {
        Comment comment = Comment.builder()
                .message(new String(new char[3001]).replace('\0', 'A'))
                .authorId("1234")
                .questionId(1L)
                .creationDate(ZonedDateTime.now())
                .build();

        List<String> validationMessages = List.of("Comments should have a maximum of 3000 characters");

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repository.create(comment))
                .matches(e -> e.getConstraintViolations().size() == validationMessages.size())
                .matches(e -> e.getConstraintViolations().stream().allMatch(
                        v -> validationMessages.contains(v.getMessage())));
    }

    @Test
    @DisplayName("should retrieve all comments from a given question id")
    void findAllBy_shouldReturnAllQuestionComments() {
        long questionId = 1L;
        entityManager.merge(from(getComment(questionId)));
        entityManager.merge(from(getComment(questionId)));
        entityManager.merge(from(getComment(2L)));

        List<Comment> result = repository.findAllBy(questionId);

        assertThat(result).hasSize(2);
    }

    private Comment getComment(long questionId) {
        return Comment.builder()
                .message("Comment")
                .authorId("1234")
                .questionId(questionId)
                .creationDate(ZonedDateTime.now())
                .build();
    }
}