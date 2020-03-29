package com.eamaral.exams.exam.domain.service;

import com.eamaral.exams.configuration.exception.ForbiddenException;
import com.eamaral.exams.configuration.exception.InvalidDataException;
import com.eamaral.exams.exam.application.dto.ExamDTO;
import com.eamaral.exams.exam.application.dto.ExamTemplateDTO;
import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.exam.domain.port.ExamRepositoryPort;
import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.application.dto.QuestionDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExamServiceTest {

    @Mock
    private ExamRepositoryPort examRepositoryPort;

    @InjectMocks
    private ExamService service;

    private final LocalDateTime startDateTime = LocalDateTime.now()
            .plusDays(2);

    private final LocalDateTime endDateTime = LocalDateTime.now()
            .plusDays(2)
            .plusMinutes(180);

    private final String currentUser = "1133444";

    @Test
    public void create_whenExamIsValid_shouldSaveANewExam_withoutSchedule() {
        Exam exam = getExamBuilderWithDefault().build();

        service.create(exam, currentUser);

        verify(examRepositoryPort).save(exam);
    }

    @Test
    public void create_whenNotMockTestAndWithoutDates_shouldThrowDatesAreRequiredWhenNotAMockTest() {
        Exam exam = getExamBuilderWithDefault()
                .startDateTime(null)
                .endDateTime(null)
                .mockTest(false)
                .build();

        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> service.create(exam, currentUser))
                .withMessage("Dates are required when not a mock test");

        verify(examRepositoryPort, never()).save(any());
    }

    @Test
    public void create_whenNotMockTestAndEndDateTimeIsNull_shouldThrowDatesAreRequiredWhenNotAMockTest() {
        Exam exam = getExamBuilderWithDefault()
                .endDateTime(null)
                .mockTest(false)
                .build();

        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> service.create(exam, currentUser))
                .withMessage("Dates are required when not a mock test");

        verify(examRepositoryPort, never()).save(any());
    }

    @Test
    public void create_whenStartDateBeforeEndDate_shouldThrowTheStartTimeMustBeBeforeTheEndTime() {
        Exam exam = getExamBuilderWithDefault()
                .startDateTime(endDateTime)
                .endDateTime(startDateTime)
                .mockTest(false)
                .build();

        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> service.create(exam, currentUser))
                .withMessage("The start time must be before the end time");

        verify(examRepositoryPort, never()).save(any());
    }

    @Test
    public void create_whenTimeIntervalBelowTo30Minutes_shouldThrowTheExamDurationMustBeAtLeast30Minutes() {
        Exam exam = getExamBuilderWithDefault()
                .startDateTime(startDateTime)
                .endDateTime(startDateTime.plusMinutes(1))
                .mockTest(false)
                .build();

        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> service.create(exam, currentUser))
                .withMessage("The exam duration must be at least 30 minutes");

        verify(examRepositoryPort, never()).save(any());
    }

    @Test
    public void create_whenDontHaveATemplate_shouldThrowExamsTemplateIsRequired() {
        Exam exam = getExamBuilderWithDefault()
                .template(null)
                .build();

        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> service.create(exam, currentUser))
                .withMessage("Exam's template is required");

        verify(examRepositoryPort, never()).save(any());
    }

    @Test
    public void create_whenAuthorDifferentOfCurrentUser_shouldThrowUnableToUseOtherUsersExamTemplate() {
        String differentUserId = "1000ZZZ";
        Exam exam = getExamBuilderWithDefault().build();

        assertThatExceptionOfType(ForbiddenException.class)
                .isThrownBy(() -> service.create(exam, differentUserId))
                .withMessage("Unable to use other users' exam template");

        verify(examRepositoryPort, never()).save(any());
    }

    @Test
    public void create_whenMockTest_shouldNotValidateDates() {
        Exam exam = getExamBuilderWithDefault()
                .startDateTime(null)
                .endDateTime(null)
                .mockTest(true)
                .build();

        service.create(exam, currentUser);

        verify(examRepositoryPort).save(exam);
    }

    @Test
    public void create_whenDatesAreInThePast_shouldThrowCouldNotCreateExamInThePast() {
        Exam exam = getExamBuilderWithDefault()
                .startDateTime(LocalDateTime.now().minusHours(1))
                .endDateTime(LocalDateTime.now().plusHours(3))
                .build();

        assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(() -> service.create(exam, currentUser))
                .withMessage("Couldn't create exam starting in the past");

        verify(examRepositoryPort, never()).save(any());
    }

    @Test
    public void findByUser_shouldRetrieveAllExamsByUser() {
        service.findByUser(currentUser);

        verify(examRepositoryPort).findByUser(currentUser);
    }

    @Test
    public void findAvailable_shouldRetrieveAllAvailableExams() {
        service.findAvailable();

        verify(examRepositoryPort).findAvailable();
    }

    private ExamDTO.ExamDTOBuilder getExamBuilderWithDefault() {
        return ExamDTO.builder()
                .template(getExamTemplate())
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .mockTest(false);
    }

    private ExamTemplateDTO getExamTemplate() {
        return ExamTemplateDTO.builder()
                .id(1L)
                .title("Exam 1")
                .author(currentUser)
                .questions(getQuestions())
                .build();
    }

    private List<QuestionDTO> getQuestions() {
        return List.of(
                QuestionDTO.builder()
                        .id(1L)
                        .type(QuestionType.TRUE_OR_FALSE)
                        .build(),
                QuestionDTO.builder()
                        .id(2L)
                        .type(QuestionType.MULTIPLE_CHOICES)
                        .build()
        );
    }
}