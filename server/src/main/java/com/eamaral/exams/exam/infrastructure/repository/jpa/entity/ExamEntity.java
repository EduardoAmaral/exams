package com.eamaral.exams.exam.infrastructure.repository.jpa.entity;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.infrastructure.repository.converter.QuestionConverter;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Entity
@Table(name = "TB_EXAM")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Where(clause = "deleted = false")
public class ExamEntity extends Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "{exam.title.required}")
    private String title;

    @Column
    private ZonedDateTime startDateTime;

    @Column
    private ZonedDateTime endDateTime;

    @Column(nullable = false)
    private boolean mockTest;

    @Column
    @NotBlank(message = "{exam.author.required}")
    private String author;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotEmpty(message = "{exam.questions.required}")
    private List<QuestionEntity> questions;

    @Column(nullable = false)
    private boolean deleted;

    public static ExamEntity from(Exam exam) {
        ExamEntityBuilder builder = builder();

        if (exam != null) {
            builder.id(exam.getId())
                    .startDateTime(exam.getStartDateTime())
                    .endDateTime(exam.getEndDateTime())
                    .mockTest(exam.isMockTest())
                    .title(exam.getTitle())
                    .author(exam.getAuthor())
                    .questions(
                            emptyIfNull(exam.getQuestions())
                                    .stream()
                                    .map(QuestionConverter::from)
                                    .collect(toList())
                    );
        }

        return builder.build();
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(emptyIfNull(questions));
    }
}
