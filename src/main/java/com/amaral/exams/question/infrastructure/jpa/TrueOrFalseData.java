package com.amaral.exams.question.infrastructure.jpa;

import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.Alternative;
import com.amaral.exams.question.domain.Question;
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
public class TrueOrFalseData extends QuestionData implements Question {

    @Builder
    public TrueOrFalseData(Long id, String statement, QuestionType type, String solution, boolean active, boolean sharable, String correctAnswer){
        super(id, statement, type, solution, active, sharable, correctAnswer);
    }

    @Override
    public List<Alternative> getAlternatives() {
        return List.of(
                AlternativeData.builder()
                        .description("True")
                        .build(),
                AlternativeData.builder()
                        .description("False")
                        .build());
    }

}
