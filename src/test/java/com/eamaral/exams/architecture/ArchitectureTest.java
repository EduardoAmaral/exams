package com.eamaral.exams.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import io.swagger.annotations.Api;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "com.eamaral.exams", importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchitectureTest {

    @ArchTest
    public static final ArchRule hexagonalArchitectureShouldBeRespected =
            layeredArchitecture()
                    .layer("Controller").definedBy("..application.controller..")
                    .layer("Service").definedBy("..domain.service..")
                    .layer("Repository").definedBy("..infrastructure.repository..")
                    .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
                    .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service");


    @ArchTest
    public static final ArchRule entitiesShouldResidesInInfrastructureJpaPackage = classes()
            .that().areAnnotatedWith(Entity.class)
            .should().resideInAPackage("..infrastructure.repository.jpa.entity..")
            .because("Entities belong to the repository layer");

    @ArchTest
    public static final ArchRule repositoriesShouldResidesInInfrastructureRepositoryPackage = classes()
            .that().areAnnotatedWith(Repository.class)
            .should().resideInAPackage("..infrastructure.repository..")
            .because("Repositories belong to the infrastructure layer");

    @ArchTest
    public static final ArchRule controllersShouldResidesInApplicationControllerPackage = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().resideInAPackage("..application.controller..")
            .because("Controllers belong to the application layer");

    @ArchTest
    public static final ArchRule servicesShouldResidesInDomainServicesPackage = classes()
            .that().haveSimpleNameEndingWith("Service")
            .should().resideInAPackage("..domain.service..")
            .because("Services belong to the domain layer");

    @ArchTest
    public static final ArchRule controllersShouldExposeApiDocumentation = classes()
            .that().areAnnotatedWith(RestController.class)
            .and().haveSimpleNameNotContaining("AuthController")
            .should().beAnnotatedWith(Api.class)
            .because("Controllers should expose API documentation");


    @ArchTest
    public static final ArchRule controllersShouldDefineRequestMapping = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().beAnnotatedWith(RequestMapping.class)
            .because("Controllers should define their path URL mapping");

}
