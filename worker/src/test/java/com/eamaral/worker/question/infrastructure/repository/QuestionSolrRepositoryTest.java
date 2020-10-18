package com.eamaral.worker.question.infrastructure.repository;

import com.eamaral.worker.configuration.solr.jpa.SolrIntegrationTest;
import com.eamaral.worker.question.domain.Question;
import com.eamaral.worker.question.infrastructure.repository.solr.QuestionSolrCrudRepository;
import com.eamaral.worker.question.infrastructure.repository.solr.QuestionSolrRepository;
import com.eamaral.worker.question.infrastructure.repository.solr.document.QuestionDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;

import java.time.Duration;
import java.util.List;

import static com.eamaral.worker.question.domain.QuestionType.MULTIPLE_CHOICES;
import static com.eamaral.worker.question.domain.QuestionType.TRUE_OR_FALSE;
import static com.eamaral.worker.question.infrastructure.repository.solr.document.QuestionDocument.COLLECTION_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class QuestionSolrRepositoryTest extends SolrIntegrationTest {

    @Autowired
    private QuestionSolrRepository repository;

    @Autowired
    private QuestionSolrCrudRepository solrCrudRepository;

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    @DisplayName("should save question documents")
    void save() {
        final List<Question> questions = List.of(Question.builder()
                        .id(1L)
                        .author("Su-ji Bae")
                        .keywords("Suzy")
                        .type(TRUE_OR_FALSE)
                        .statement("Statement 1")
                        .subject("Korean")
                        .build(),
                Question.builder()
                        .id(2L)
                        .author("Min-young Park")
                        .keywords("Rachel")
                        .type(MULTIPLE_CHOICES)
                        .statement("Statement 2")
                        .subject("Korean")
                        .build()
        );

        repository.save(questions);

        final Page<QuestionDocument> documents = solrTemplate.query(COLLECTION_NAME, new SimpleQuery("*"), QuestionDocument.class);

        assertThat(documents)
                .extracting(QuestionDocument::getId,
                        QuestionDocument::getAuthor,
                        QuestionDocument::getKeywords,
                        QuestionDocument::getStatement,
                        QuestionDocument::getSubject,
                        QuestionDocument::getType)
                .containsExactlyInAnyOrder(
                        tuple(1L, "Su-ji Bae", "Suzy", "Statement 1", "Korean", TRUE_OR_FALSE.getDescription()),
                        tuple(2L, "Min-young Park", "Rachel", "Statement 2", "Korean", MULTIPLE_CHOICES.getDescription())
                );
    }

    @Test
    @DisplayName("should remove all documents on solr")
    void deleteAll() {
        final QuestionDocument document = QuestionDocument.builder()
                .id(1L)
                .author("Su-ji Bae")
                .keywords("Suzy")
                .type(TRUE_OR_FALSE.getDescription())
                .statement("Statement 1")
                .subject("Korean")
                .build();

        solrCrudRepository.save(document, Duration.ZERO);

        final Page<QuestionDocument> result = solrTemplate.query(COLLECTION_NAME, new SimpleQuery("*"), QuestionDocument.class);
        assertThat(result).hasSize(1);

        repository.deleteAll();

        final Page<QuestionDocument> resultAfterDelete = solrTemplate.query(COLLECTION_NAME, new SimpleQuery("*"), QuestionDocument.class);
        assertThat(resultAfterDelete).isEmpty();
    }
}