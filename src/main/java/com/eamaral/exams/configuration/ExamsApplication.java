package com.eamaral.exams.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.eamaral.exams"})
@EntityScan(basePackages = {"com.eamaral.exams.**.jpa"})
@EnableJpaRepositories(basePackages = {"com.eamaral.exams.**.jpa"})
public class ExamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamsApplication.class, args);
	}

}
