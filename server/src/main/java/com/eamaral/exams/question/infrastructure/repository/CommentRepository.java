package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.port.CommentRepositoryPort;
import com.eamaral.exams.question.infrastructure.repository.jpa.CommentJpaRepository;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.CommentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class CommentRepository implements CommentRepositoryPort {

    private final CommentJpaRepository repository;

    public CommentRepository(CommentJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Comment create(Comment comment) {
        return repository.saveAndFlush(CommentEntity.from(comment))
                .toComment();
    }

    @Override
    public List<Comment> findAllBy(Long questionId) {
        return repository.findAllByQuestionId(questionId).stream()
                .map(CommentEntity::toComment)
                .collect(toList());
    }
}
