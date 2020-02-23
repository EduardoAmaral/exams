package com.amaral.exams.question.infrastructure.jpa.entity;

import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.Alternative;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "TB_QUESTION_MULTIPLE_CHOICE")
@Getter
@NoArgsConstructor
public class MultipleChoiceEntity extends QuestionEntity {

    @Column
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
            boolean active,
            boolean sharable,
            String correctAnswer,
            String topic,
            List<AlternativeEntity> alternatives){
        super(id, statement, type, solution, active, sharable, correctAnswer, topic);
        this.alternatives = alternatives;
    }

    @Override
    public List<Alternative> getAlternatives(){
        return new ArrayList<>(alternatives);
    }

}
