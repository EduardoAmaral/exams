package com.amaral.exams.question.infrastructure;

import com.amaral.exams.configuration.jpa.JPAIntegrationTest;
import com.amaral.exams.question.infrastructure.jpa.entity.SubjectEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubjectRepositoryTest extends JPAIntegrationTest {

    @Autowired
    private SubjectRepository repository;

    @Test
    public void save_shouldSaveASubject() {
        SubjectEntity entity = SubjectEntity.builder()
                .description("English")
                .build();

        entity = repository.save(entity);

        assertThat(entity.getId()).isNotZero();
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

        List<SubjectEntity> result = repository.findAll();

        assertThat(result).hasSize(2);
    }
}
