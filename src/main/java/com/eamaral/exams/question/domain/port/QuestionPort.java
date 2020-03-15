package com.eamaral.exams.question.domain.port;

import com.eamaral.exams.question.domain.Question;

import java.util.List;

public interface QuestionPort {

    List<Question> findByUser(String author);

    Question find(String id);

    void save(Question question);

    List<Question> saveAll(List<Question> question);

    Question update(Question question);

    void delete(String id, String currentUserId);

    List<Question> search(Question criteria, String currentUser);
}
