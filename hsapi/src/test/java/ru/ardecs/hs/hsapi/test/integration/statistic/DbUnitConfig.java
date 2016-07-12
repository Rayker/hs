package ru.ardecs.hs.hsapi.test.integration.statistic;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"ru.ardecs.hs.hsdb.repositories"})
public class DbUnitConfig {
}
