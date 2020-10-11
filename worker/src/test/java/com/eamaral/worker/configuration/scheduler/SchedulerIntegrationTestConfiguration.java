package com.eamaral.worker.configuration.scheduler;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan(
        useDefaultFilters = false,
        basePackages = {"com.eamaral.worker.**.application.**.scheduler"},
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class)
        }
)
@EnableScheduling
public class SchedulerIntegrationTestConfiguration {
}
