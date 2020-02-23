package com.amaral.exams.question.infrastructure;

import com.amaral.exams.configuration.jpa.JPAIntegrationTest;
import com.amaral.exams.question.infrastructure.jpa.entity.SubjectEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}
