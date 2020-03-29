package com.eamaral.exams.exam.infrastructure.repository.jpa.entity;

import com.eamaral.exams.exam.domain.Exam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_EXAM")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "active = true")
public class ExamEntity extends Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamTemplateEntity template;

    @Column
    private LocalDateTime startDateTime;

    @Column
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private boolean mockTest;

    @Column(nullable = false)
    private boolean active;

    public static ExamEntity from(Exam exam) {
        ExamEntityBuilder builder = builder();

        if (exam != null) {
            builder.id(exam.getId())
                    .active(true)
                    .startDateTime(exam.getStartDateTime())
                    .endDateTime(exam.getEndDateTime())
                    .mockTest(exam.isMockTest())
                    .template(ExamTemplateEntity.from(exam.getTemplate()));
        }

        return builder.build();
    }
}
