package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.question.domain.Question;
import com.eamaral.exams.question.domain.port.QuestionRepositoryPort;
import com.eamaral.exams.question.infrastructure.repository.jpa.AlternativeJpaRepository;
import com.eamaral.exams.question.infrastructure.repository.jpa.QuestionJpaRepository;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.AlternativeEntity;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.QuestionEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.eamaral.exams.question.infrastructure.repository.jpa.QuestionSpecification.matchFilters;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@AllArgsConstructor
public class QuestionRepository implements QuestionRepositoryPort {

    private final QuestionJpaRepository repository;
    private final AlternativeJpaRepository alternativeRepository;

    @Override
    public List<Question> findByUser(String currentUser) {
        return repository.findAllByAuthorId(currentUser)
                .stream()
                .map(QuestionEntity::toDomain)
                .collect(toList());
    }

    @Override
    public Optional<Question> find(Long id, String currentUser) {
        Optional<QuestionEntity> question = repository
                .findById(id);
        return question.map(QuestionEntity::toDomain);
    }

    @Override
    public Question save(Question question) {
        final QuestionEntity savedQuestion = repository.saveAndFlush(
                QuestionEntity.from(question)
        );

        final List<AlternativeEntity> alternatives = alternativeRepository.saveAll(
                AlternativeEntity.withId(question.getAlternatives(), savedQuestion.getId()));

        return savedQuestion.toBuilder()
                .alternatives(alternatives)
                .build()
                .toDomain();
    }

    @Override
    public List<Question> saveAll(List<Question> questions) {
        List<QuestionEntity> questionsData = questions.stream().map(QuestionEntity::from).collect(toList());
        return repository.saveAll(questionsData)
                .stream()
                .map(QuestionEntity::toDomain)
                .collect(toList());
    }

    @Override
    public List<Question> findByCriteria(Question criteria, String currentUser) {
        QuestionEntity query = QuestionEntity.from(criteria);

        return repository.findAll(
                where(matchFilters(query)))
                .stream()
                .map(QuestionEntity::toDomain)
                .collect(toList());
    }

    @Override
    @Transactional
    public void delete(Question question) {
        repository.softDeleteQuestion(question.getId());
    }

}
