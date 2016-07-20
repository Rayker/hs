package ru.ardecs.hs.hsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"ru.ardecs.hs.hsdb.repositories"})
@EnableScheduling
@EnableTransactionManagement
@EntityScan("ru.ardecs.hs.hscommon.*")
@ComponentScan(basePackages = {"ru.ardecs.hs.hsapi.*", "ru.ardecs.hs.*"})
public class HsapiApplication {
	public static void main(String[] args) {
		SpringApplication.run(HsapiApplication.class, args);
	}
}
