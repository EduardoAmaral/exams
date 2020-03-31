package com.eamaral.exams.exam.domain.port;

import com.eamaral.exams.exam.domain.Exam;

import java.util.List;
import java.util.Optional;

public interface ExamRepositoryPort {

    Exam save(Exam exam);

    List<Exam> findByUser(String currentUser);

    List<Exam> findAvailable();

    Optional<Exam> findById(Long id, String currentUser);

    void delete(Exam exam);
}
