package com.eamaral.exams.question.domain.service;

import com.eamaral.exams.question.domain.Subject;
import com.eamaral.exams.question.domain.port.SubjectRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @InjectMocks
    private SubjectService service;

    @Mock
    private SubjectRepositoryPort repository;

    @Test
    @DisplayName("should save a subject")
    void save_shouldSaveASubject() {
        Subject subject = Subject.builder()
                .description("English")
                .build();

        when(repository.save(subject)).thenReturn(subject);

        subject = service.save(subject);

        assertThat(subject).isNotNull();
    }

    @Test
    @DisplayName("should retrieve all subjects")
    void findAll_shouldReturnAllSubjects_orderedByDescription() {
        List<Subject> subjects = List.of(
                Subject.builder()
                        .description("French")
                        .build(),
                Subject.builder()
                        .description("English")
                        .build(),
                Subject.builder()
                        .description("Chinese")
                        .build()
        );

        when(repository.findAll()).thenReturn(subjects);

        assertThat(service.findAll())
                .extracting(Subject::getDescription)
                .containsExactly("Chinese", "English", "French");
    }
}
