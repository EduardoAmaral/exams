package com.eamaral.worker.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"com.eamaral.worker.**.jpa"})
@EnableJpaRepositories(basePackages = {"com.eamaral.worker.**.jpa"})
public class JpaConfiguration {
}
