package com.eamaral.worker.question.infrastructure.repository;

import com.eamaral.worker.configuration.jpa.JpaIntegrationTest;
import com.eamaral.worker.question.domain.Question;
import com.eamaral.worker.question.infrastructure.repository.jpa.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

import static com.eamaral.worker.question.domain.QuestionType.MULTIPLE_CHOICES;
import static com.eamaral.worker.question.domain.QuestionType.TRUE_OR_FALSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class QuestionRepositoryTest extends JpaIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private QuestionRepository repository;

    @Test
    @DisplayName("should get all non-deleted questions")
    void findAll() {
        final List<Question> questions = repository.findAll();

        assertThat(questions).extracting(
                Question::getId,
                Question::getStatement,
                Question::getType,
                Question::getSubject,
                Question::getKeywords,
                Question::getAuthor)
                .containsExactlyInAnyOrder(
                        tuple(1L, "Statement 1", TRUE_OR_FALSE, "Subject", "Key 1; Key 2;", "Bo-young Park"),
                        tuple(3L, "Statement 3", MULTIPLE_CHOICES, "Subject", "Key 7; Key 8;", "Na-ra Jang")
                );
    }

}