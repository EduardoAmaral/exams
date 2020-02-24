package com.amaral.exams.question.infrastructure;

import com.amaral.exams.configuration.jpa.JpaIntegrationTest;
import com.amaral.exams.question.domain.Subject;
import com.amaral.exams.question.infrastructure.jpa.entity.SubjectEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SubjectRepositoryTest extends JpaIntegrationTest {

    @Autowired
    private SubjectRepository repository;

    @Test
    public void save_shouldSaveASubject() {
        SubjectEntity entity = SubjectEntity.builder()
                .description("English")
                .build();

        Subject subject = repository.save(entity);

        assertThat(subject.getId()).isNotZero();
    }

    @Test
    public void findAll_shouldReturnAllSubjects() {
        SubjectEntity english = SubjectEntity.builder()
                .description("English")
                .build();

        SubjectEntity french = SubjectEntity.builder()
                .description("French")
                .build();

        repository.save(english);
        repository.save(french);

        List<Subject> result = repository.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    public void save_whenDescriptionIsBlank_shouldThrowsException() {
        SubjectEntity entity = new SubjectEntity();
        assertThatThrownBy(() -> repository.save(entity))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
