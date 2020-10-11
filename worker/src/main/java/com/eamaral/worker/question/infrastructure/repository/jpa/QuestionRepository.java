package com.eamaral.worker.question.infrastructure.repository.jpa;

import com.eamaral.worker.question.domain.Question;
import com.eamaral.worker.question.domain.port.QuestionPort;
import com.eamaral.worker.question.infrastructure.repository.jpa.entity.QuestionEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
@AllArgsConstructor
public class QuestionRepository implements QuestionPort {

    private final QuestionJpaRepository repository;

    @Override
    public List<Question> findAll() {
        return repository.findAll().stream()
                .map(QuestionEntity::toDomain)
                .collect(toList());
    }
}
