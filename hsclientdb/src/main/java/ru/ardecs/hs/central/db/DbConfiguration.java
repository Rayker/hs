package ru.ardecs.hs.central.db;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.ardecs.hs.central.db.repositories")
@EntityScan(basePackages = {"ru.ardecs.hs.central.db.entities", "ru.ardecs.hs.common.entities.shared"})
public class DbConfiguration {
}
