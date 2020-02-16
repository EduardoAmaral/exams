package com.amaral.exams.question.infrastructure.jpa;

import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.Alternative;
import com.amaral.exams.question.domain.Question;
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
public class MultipleChoiceData extends QuestionData implements Question {

    @Column
    @NotEmpty(message = "{question.alternatives.required}")
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<AlternativeData> alternatives;

    @Builder
    public MultipleChoiceData(
            Long id,
            String statement,
            QuestionType type,
            String solution,
            boolean active,
            boolean sharable,
            String correctAnswer,
            List<AlternativeData> alternatives){
        super(id, statement, type, solution, active, sharable, correctAnswer);
        this.alternatives = alternatives;
    }

    @Override
    public List<Alternative> getAlternatives(){
        return new ArrayList<>(alternatives);
    }

}
