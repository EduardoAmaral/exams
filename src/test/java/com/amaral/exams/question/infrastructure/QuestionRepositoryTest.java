package com.amaral.exams.question.infrastructure;

import com.amaral.exams.configuration.jpa.JPAIntegrationTest;
import com.amaral.exams.question.QuestionType;
import com.amaral.exams.question.domain.services.Question;
import com.amaral.exams.question.infrastructure.jpa.QuestionData;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionRepositoryTest extends JPAIntegrationTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void save_whenUsingValidFields_shouldReturnSuccess() {
        Question question = Question.builder().statement("Can I test it?").type(QuestionType.TRUE_OR_FALSE).build();

        question = questionRepository.save(question);

        Assertions.assertThat(question.getId()).isNotZero();
    }

}