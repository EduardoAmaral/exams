package com.eamaral.exams.question.infrastructure.repository.jpa.entity;

import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Alternative;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class TrueOrFalseEntity extends QuestionEntity {

    @Builder
    public TrueOrFalseEntity(Long id,
                             String statement,
                             QuestionType type,
                             String solution,
                             boolean deleted,
                             boolean shared,
                             String correctAnswer,
                             String topic,
                             SubjectEntity subject,
                             String author) {
        super(id, statement, type, solution, deleted, shared, correctAnswer, topic, subject, author);
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
