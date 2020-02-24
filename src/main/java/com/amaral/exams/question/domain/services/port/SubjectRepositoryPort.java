package com.amaral.exams.question.domain.services.port;

        import com.amaral.exams.question.domain.Subject;

        import java.util.List;

public interface SubjectRepositoryPort {

    Subject save(Subject subject);

    List<Subject> findAll();
}
