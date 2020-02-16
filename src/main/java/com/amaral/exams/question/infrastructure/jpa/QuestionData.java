package com.amaral.exams.question.infrastructure.jpa;

import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.services.Question;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_QUESTION")
public class QuestionData implements Question {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(2000)")
    @NotEmpty(message = "Statement is required")
    @Size(min = 4, max = 2000, message = "The statement should have between 4 and 2000 characters")
    private String statement;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type is required")
    private QuestionType type;

    @Column(columnDefinition = "varchar(3000)")
    @Max(value = 3000, message = "The maximum value to solution is 3000 characters")
    private String solution;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean sharable;

    public static QuestionData from(Question question){
        return QuestionData.builder()
                .id(question.getId())
                .statement(question.getStatement())
                .active(question.isActive())
                .solution(question.getSolution())
                .type(question.getType())
                .sharable(question.isSharable())
                .build();
    }

}
