package com.eamaral.exams.configuration.jpa;

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

@Configuration
@ImportAutoConfiguration(classes = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ComponentScan(
        useDefaultFilters = false,
        basePackages = {"com.eamaral.exams.**.infrastructure", "com.eamaral.exams.**.jpa"},
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class)
        }
)
@EntityScan(basePackages = {"com.eamaral.exams.**.jpa"})
@EnableJpaRepositories(basePackages = {"com.eamaral.exams.**.jpa"})
public class JpaIntegrationTestConfiguration {

}
