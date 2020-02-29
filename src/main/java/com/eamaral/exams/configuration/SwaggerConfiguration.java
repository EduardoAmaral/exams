package com.eamaral.exams.configuration;

import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.time.LocalDate;
import java.util.Collections;

@Configuration
@EnableSwagger2WebMvc
@Import({ BeanValidatorPluginsConfiguration.class })
public class SwaggerConfiguration {

    @Bean
    public Docket docker() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .directModelSubstitute(LocalDate.class, String.class)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Exams API",
                "An API to create and apply Exams",
                "0.0.1-SNAPSHOT",
                "Terms of service",
                new Contact("Eduardo Amaral",
                        "https://github.com/EduardoAmaral/",
                        "eduardofsamaral@gmail.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                Collections.emptyList());
    }

}
