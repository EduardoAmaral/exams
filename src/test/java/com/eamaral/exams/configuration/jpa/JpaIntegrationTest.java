package com.eamaral.exams.configuration.jpa;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { JpaIntegrationTestConfiguration.class }, properties = "spring.jpa.open-in-view=false")
@Transactional
public abstract class JpaIntegrationTest {

}
