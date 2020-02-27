package com.eamaral.exams.configuration.controller;

import com.eamaral.exams.configuration.ValidationConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
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
@ImportAutoConfiguration(ValidationConfiguration.class)
public class ControllerTestConfiguration {

}
