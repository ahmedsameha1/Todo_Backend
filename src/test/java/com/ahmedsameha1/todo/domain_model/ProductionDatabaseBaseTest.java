package com.ahmedsameha1.todo.domain_model;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = {ProductionDatabaseBaseTest.PropertiesInitializer.class})
public abstract class ProductionDatabaseBaseTest {
    @Container
    protected static final PostgreSQLContainer postgres = new PostgreSQLContainer();

    static {
        postgres.withDatabaseName("users")
                .withUsername("postgres")
                .withPassword("password");
        postgres.setDockerImageName("fintrace/postgres-uuid");
        postgres.withInitScript("sc.sql");
    }

    static class PropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgres.getJdbcUrl(),
                    "spring.datasource.username=" + postgres.getUsername(),
                    "spring.datasource.password=" + postgres.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}