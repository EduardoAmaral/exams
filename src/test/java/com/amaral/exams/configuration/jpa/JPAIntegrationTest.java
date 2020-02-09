package com.amaral.exams.configuration.jpa;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = { JPAIntegrationTestConfiguration.class }, properties = "spring.jpa.open-in-view=false")
public abstract class JPAIntegrationTest {

}
