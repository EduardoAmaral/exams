package com.eamaral.exams.question.domain.services.port;

        import com.eamaral.exams.question.domain.Subject;

        import java.util.List;

public interface SubjectRepositoryPort {

    Subject save(Subject subject);

    List<Subject> findAll();
}
