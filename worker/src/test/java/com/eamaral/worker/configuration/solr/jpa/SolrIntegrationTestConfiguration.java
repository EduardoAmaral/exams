package com.eamaral.worker.configuration.solr.jpa;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreDescriptor;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
@ImportAutoConfiguration(classes = {
        SolrRepositoriesAutoConfiguration.class,
        SolrAutoConfiguration.class
})
@ComponentScan(
        useDefaultFilters = false,
        basePackages = {"com.eamaral.worker.**.solr"},
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class)
        }
)
@EnableSolrRepositories(basePackages = {"com.eamaral.worker.**.solr"})
public class SolrIntegrationTestConfiguration {

    private static final List<String> COLLECTIONS = List.of("questions");

    private final SolrClient server;
    private Path solrHome;

    public SolrIntegrationTestConfiguration() throws IOException {
        server = setupClient();
    }

    @Bean
    public SolrClient solrClient() {
        return server;
    }

    @PreDestroy
    public void shutdown() throws IOException {
        server.close();
        FileUtils.deleteQuietly(solrHome.toFile());
    }

    private SolrClient setupClient() throws IOException {
        solrHome = Files.createTempDirectory(UUID.randomUUID().toString());
        copyConfig(solrHome);

        final EmbeddedSolrServer server = new EmbeddedSolrServer(solrHome, "questions");

        createCollections(server);
        return server;
    }

    private void copyConfig(Path solrHome) throws IOException {
        FileUtils.copyDirectory(
                Path.of("../solr").toFile(),
                solrHome.toFile()
        );
    }

    private void createCollections(EmbeddedSolrServer server) {
        for (String collection : COLLECTIONS) {
            if (server.getCoreContainer().getAllCoreNames().contains(collection)) continue;

            server.getCoreContainer()
                    .create(collection,
                            solrHome.resolve("configsets/questions"),
                            Map.of(CoreDescriptor.CORE_CONFIGSET, collection,
                                    CoreDescriptor.CORE_SCHEMA, "managed-schema"),
                            false);
        }
    }

}
