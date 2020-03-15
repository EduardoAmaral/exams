package com.eamaral.exams.exam.infrastructure.repository.jpa.entity;

import com.eamaral.exams.exam.domain.Exam;
import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.infrastructure.repository.converter.QuestionConverter;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "TB_EXAM")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "active = true")
public class ExamEntity implements Exam {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @NotBlank(message = "{exam.title.required}")
    @Column
    private String title;

    @NotBlank(message = "{exam.author.required}")
    @Column
    private String author;

    @Column
    @NotEmpty(message = "{exam.questions.required}")
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private List<QuestionEntity> questions;

    @Column(nullable = false)
    private boolean active;

    public static ExamEntity from(Exam exam) {
        ExamEntityBuilder builder = builder();

        if (exam != null) {
            builder.author(exam.getAuthor())
                    .active(true)
                    .title(exam.getTitle());

            if (exam.getQuestions() != null) {
                builder.questions(exam.getQuestions()
                        .stream()
                        .map(QuestionConverter::from)
                        .collect(toList()));
            }
        }

        return builder.build();
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

}
