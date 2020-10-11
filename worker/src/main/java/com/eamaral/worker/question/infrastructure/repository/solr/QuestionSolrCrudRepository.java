package com.eamaral.worker.question.infrastructure.repository.solr;

import com.eamaral.worker.question.infrastructure.repository.solr.document.QuestionDocument;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface QuestionSolrCrudRepository extends SolrCrudRepository<QuestionDocument, Long> {
}
