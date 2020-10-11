package com.eamaral.worker.configuration.jpa;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JpaIntegrationTestConfiguration.class}, properties = "spring.jpa.open-in-view=false")
public abstract class JpaIntegrationTest {

}
