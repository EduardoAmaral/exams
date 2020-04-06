package com.eamaral.exams.question.infrastructure.repository.jpa.entity;

import com.eamaral.exams.question.QuestionType;
import com.eamaral.exams.question.domain.Alternative;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class MultipleChoiceEntity extends QuestionEntity {

    @NotEmpty(message = "{question.alternatives.required}")
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<AlternativeEntity> alternatives;

    @Builder
    public MultipleChoiceEntity(
            Long id,
            String statement,
            QuestionType type,
            String solution,
            boolean deleted,
            boolean shared,
            String correctAnswer,
            String topic,
            SubjectEntity subject,
            String author,
            List<AlternativeEntity> alternatives) {
        super(id, statement, type, solution, deleted, shared, correctAnswer, topic, subject, author);
        this.alternatives = alternatives;
    }

    @Override
    public List<Alternative> getAlternatives() {
        return new ArrayList<>(alternatives);
    }

}
