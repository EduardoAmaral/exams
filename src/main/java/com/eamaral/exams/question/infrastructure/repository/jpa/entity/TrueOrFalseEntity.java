package com.eamaral.exams.question.infrastructure.repository.jpa.entity;

import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Alternative;
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
                             String topic,
                             SubjectEntity subject,
                             String author) {
        super(id, statement, type, solution, active, sharable, correctAnswer, topic, subject, author);
    }

    @Override
    public List<Alternative> getAlternatives() {
        return List.of(
                AlternativeEntity.builder()
                        .id(1L)
                        .description("True")
                        .build(),
                AlternativeEntity.builder()
                        .id(2L)
                        .description("False")
                        .build());
    }

}
