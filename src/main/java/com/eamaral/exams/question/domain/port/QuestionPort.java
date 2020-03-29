package com.eamaral.exams.question.domain.port;

import com.eamaral.exams.question.domain.Question;

import java.util.List;

public interface QuestionPort {

    List<Question> findByUser(String currentUser);

    Question find(Long id, String currentUser);

    void save(Question question);

    List<Question> saveAll(List<Question> question);

    Question update(Question question, String currentUser);

    void delete(Long id, String currentUser);

    List<Question> search(Question criteria, String currentUser);
}
