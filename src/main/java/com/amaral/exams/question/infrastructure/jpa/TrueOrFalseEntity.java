package com.amaral.exams.question.infrastructure.jpa;

import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.Alternative;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "TB_QUESTION_TRUE_OR_FALSE")
@Getter
@NoArgsConstructor
public class TrueOrFalseEntity extends QuestionEntity {

    @Builder
    public TrueOrFalseEntity(Long id,
                             String statement,
                             QuestionType type,
                             String solution,
                             boolean active,
                             boolean sharable,
                             String correctAnswer,
                             String topic){
        super(id, statement, type, solution, active, sharable, correctAnswer, topic);
    }

    @Override
    public List<Alternative> getAlternatives() {
        return List.of(
                AlternativeEntity.builder()
                        .description("True")
                        .build(),
                AlternativeEntity.builder()
                        .description("False")
                        .build());
    }

}
