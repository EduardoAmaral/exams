package com.eamaral.worker.question.infrastructure.repository.solr.document;

import com.eamaral.worker.question.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import static com.eamaral.worker.question.infrastructure.repository.solr.document.QuestionDocument.COLLECTION_NAME;

@Getter
@Builder
@AllArgsConstructor
@SolrDocument(collection = COLLECTION_NAME)
public class QuestionDocument {

    public static final String COLLECTION_NAME = "questions";

    @Id
    @Indexed
    private final Long id;

    @Indexed
    private final String statement;

    @Indexed
    private final String author;

    @Indexed
    private final String keywords;

    @Indexed
    private final String subject;

    @Indexed
    private final String type;

    public static QuestionDocument from(Question question) {
        return builder()
                .id(question.getId())
                .author(question.getAuthor())
                .statement(question.getStatement())
                .keywords(question.getKeywords())
                .subject(question.getSubject())
                .type(question.getType().getDescription())
                .build();
    }
}
