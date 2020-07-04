package com.eamaral.exams.configuration.jpa;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = {JpaIntegrationTestConfiguration.class}, properties = "spring.jpa.open-in-view=false")
@Transactional
public abstract class JpaIntegrationTest {

}
