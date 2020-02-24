package com.amaral.exams.question.domain.services;

import com.amaral.exams.question.domain.Subject;
import com.amaral.exams.question.domain.services.port.SubjectRepositoryPort;
import com.amaral.exams.question.infrastructure.jpa.entity.SubjectEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubjectServiceTest {

    @InjectMocks
    private SubjectService service;

    @Mock
    private SubjectRepositoryPort repository;

    @Test
    public void save_shouldSaveASubject() {
        Subject subject = SubjectEntity.builder()
                .description("English")
                .build();

        when(repository.save(subject)).thenReturn(subject);

        subject = service.save(subject);

        assertThat(subject).isNotNull();
    }
}
