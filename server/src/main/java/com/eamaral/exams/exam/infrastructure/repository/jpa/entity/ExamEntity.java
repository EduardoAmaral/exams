package com.eamaral.exams.exam.infrastructure.repository.jpa.entity;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.question.domain.Question;
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

    @Column(name = "STARTS_AT")
    private ZonedDateTime startDateTime;

    @Column(name = "ENDS_AT")
    private ZonedDateTime endDateTime;

    @Column(name = "AUTHOR_ID")
    @NotBlank(message = "{exam.author.required}")
    private String authorId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_EXAM_QUESTION",
            joinColumns = {@JoinColumn(name = "EXAM_ID")},
            inverseJoinColumns = {@JoinColumn(name = "QUESTION_ID")})
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
                    .title(exam.getTitle())
                    .authorId(exam.getAuthorId())
                    .questions(
                            emptyIfNull(exam.getQuestions())
                                    .stream()
                                    .map(QuestionEntity::from)
                                    .collect(toList())
                    );
        }

        return builder.build();
    }

    public List<Question> getQuestions() {
        return emptyIfNull(questions).stream()
                .map(QuestionEntity::toDomain)
                .collect(toList());
    }
}
