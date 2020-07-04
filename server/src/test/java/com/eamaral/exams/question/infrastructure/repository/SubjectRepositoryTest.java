package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.configuration.jpa.JpaIntegrationTest;
import com.eamaral.exams.question.domain.Subject;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.SubjectEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubjectRepositoryTest extends JpaIntegrationTest {

    @Autowired
    private SubjectRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("should save a subject")
    void save_shouldSaveASubject() {
        SubjectEntity entity = SubjectEntity.builder()
                .description("English")
                .build();

        Subject subject = repository.save(entity);

        assertThat(subject.getId()).isNotZero();
    }

    @Test
    @DisplayName("should retrieve all the subjects registered")
    void findAll_shouldReturnAllSubjects() {
        SubjectEntity english = SubjectEntity.builder()
                .description("English")
                .build();

        SubjectEntity french = SubjectEntity.builder()
                .description("French")
                .build();

        entityManager.merge(english);
        entityManager.merge(french);

        List<Subject> result = repository.findAll();

        assertThat(result).hasSize(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "    "})
    @DisplayName("should validate that a subject can't be saved without description")
    void save_whenDescriptionIsBlank_shouldThrowsException(String description) {
        assertThatThrownBy(() -> repository.save(SubjectEntity.builder()
                .description(description)
                .build()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("should validate ")
    void save_whenDescriptionAlreadyExists_shouldThrowsException() {
        SubjectEntity english = SubjectEntity.builder()
                .description("English")
                .build();

        repository.save(english);

        assertThatThrownBy(() -> repository.save(english))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
