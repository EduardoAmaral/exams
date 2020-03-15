package com.eamaral.exams.exam.domain.service;

import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.configuration.exception.NotFoundException;
import com.eamaral.exams.exam.application.dto.ExamDTO;
import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.domain.port.ExamRepositoryPort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExamServiceTest {

    @Mock
    private ExamRepositoryPort examRepository;

    @InjectMocks
    private ExamService service;

    @Test
    public void save_shouldReturnSuccess() {
        Exam exam = getExam();

        service.save(exam);

        verify(examRepository).save(exam);
    }

    @Test
    public void findByUser_shouldReturnExamsCreatedByTheUser() {
        String currentUser = "10001";

        when(examRepository.findByUser(currentUser)).thenReturn(List.of(getExam()));

        List<Exam> exams = service.findByUser(currentUser);

        verify(examRepository).findByUser(currentUser);

        assertThat(exams).isNotEmpty();
    }

    @Test
    public void findById_shouldReturnTheExam_whenItExists() {
        String examId = "1";
        String currentUser = "10001";

        when(examRepository.findById(examId, currentUser)).thenReturn(Optional.of(getExam()));

        Exam exam = service.findById(examId, currentUser);

        verify(examRepository).findById(examId, currentUser);

        assertThat(exam).isNotNull();
    }

    @Test
    public void findById_whenNothingIsFound_shouldThrowNotFoundException() {
        String examId = "1";
        String currentUser = "10001";

        when(examRepository.findById(examId, currentUser)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(examId, currentUser))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Exam 1 was not found");

        verify(examRepository).findById(examId, currentUser);
    }

    @Test
    public void findById_whenIdIsNull_shouldThrowInvalidDataException() {
        String currentUser = "10001";

        assertThatThrownBy(() -> service.findById(null, currentUser))
                .isInstanceOf(InvalidDataException.class)
                .hasMessage("Exam's id is required");

        verify(examRepository, never()).findById(null, currentUser);
    }

    private Exam getExam() {
        return ExamDTO.builder()
                .title("Exam 1")
                .author("10001")
                .questions(emptyList())
                .build();
    }

}