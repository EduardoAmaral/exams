package com.eamaral.worker.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(basePackages = {"com.eamaral.worker.**.solr"})
public class SolrConfiguration {

}
