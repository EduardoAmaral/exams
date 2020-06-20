package com.eamaral.exams.question.infrastructure.repository;

import com.eamaral.exams.question.domain.Comment;
import com.eamaral.exams.question.domain.port.CommentRepositoryPort;
import com.eamaral.exams.question.infrastructure.repository.jpa.CommentJpaRepository;
import com.eamaral.exams.question.infrastructure.repository.jpa.entity.CommentEntity;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepository implements CommentRepositoryPort {

    private final CommentJpaRepository repository;

    public CommentRepository(CommentJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Comment create(Comment comment) {
        return repository.saveAndFlush(CommentEntity.from(comment));
    }
}
