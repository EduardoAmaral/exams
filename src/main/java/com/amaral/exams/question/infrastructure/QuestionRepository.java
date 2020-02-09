package com.amaral.exams.question.infrastructure;

import com.amaral.exams.question.domain.services.Question;
import com.amaral.exams.question.domain.services.port.QuestionRepositoryPort;
import com.amaral.exams.question.infrastructure.jpa.QuestionData;
import com.amaral.exams.question.infrastructure.jpa.QuestionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.amaral.exams.question.infrastructure.jpa.QuestionData.from;
import static java.util.stream.Collectors.toList;

@Repository
public class QuestionRepository implements QuestionRepositoryPort {

    private QuestionJpaRepository repository;

    @Autowired
    public QuestionRepository(QuestionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Question> findAll() {
        return repository.findAll()
                .stream()
                .map(QuestionData::toDomain)
                .collect(toList());
    }

    @Override
    public Question findById(Long id) {
        return repository
                .findById(id)
                .get()
                .toDomain();
    }

    @Override
    public Question save(Question question) {
        QuestionData newQuestion = repository.saveAndFlush(from(question));

        return newQuestion.toDomain();
    }
}
