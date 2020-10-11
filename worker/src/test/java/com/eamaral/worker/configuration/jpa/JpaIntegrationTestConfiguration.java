package com.eamaral.worker.configuration.jpa;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ImportAutoConfiguration(classes = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ComponentScan(
        useDefaultFilters = false,
        basePackages = {"com.eamaral.worker.**.jpa"},
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class)
        }
)
@EntityScan(basePackages = {"com.eamaral.worker.**.jpa"})
@EnableJpaRepositories(basePackages = {"com.eamaral.worker.**.jpa"})
@EnableTransactionManagement
public class JpaIntegrationTestConfiguration {

}
