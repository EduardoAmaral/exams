package com.eamaral.worker.question.infrastructure.repository.solr;

import com.eamaral.worker.question.domain.Question;
import com.eamaral.worker.question.domain.port.QuestionDocumentPort;
import com.eamaral.worker.question.infrastructure.repository.solr.document.QuestionDocument;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
@AllArgsConstructor
public class QuestionSolrRepository implements QuestionDocumentPort {

    private final QuestionSolrCrudRepository repository;

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void save(List<Question> questions) {
        final List<QuestionDocument> documents = questions.stream()
                .map(QuestionDocument::from)
                .collect(toList());

        repository.saveAll(documents);
    }

}
