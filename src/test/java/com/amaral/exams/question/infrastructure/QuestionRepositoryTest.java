package com.amaral.exams.question.infrastructure;

import com.amaral.exams.configuration.jpa.JPAIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionRepositoryTest extends JPAIntegrationTest {

    @Autowired
    private QuestionRepository questionRepository;

    public void save_whenUsingValidFields_shouldReturnSuccess() {

    }
}