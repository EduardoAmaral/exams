package com.eamaral.exams.configuration.controller;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ComponentScan(
        useDefaultFilters = false,
        basePackages = {"com.eamaral.exams.**.application.**.controllers"},
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = RestController.class)
        }
)
@ImportAutoConfiguration(ValidationAutoConfiguration.class)
public class ControllerTestConfiguration {

}
