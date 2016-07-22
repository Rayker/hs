package ru.ardecs.hs.city;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"ru.ardecs.hs.city.db.repositories"})
@EnableScheduling
@EnableTransactionManagement
@EntityScan("ru.ardecs.hs.common.entities")
@ComponentScan(basePackages = {"ru.ardecs.hs.city.*", "ru.ardecs.hs.*"})
public class HsCityApplication {
	public static void main(String[] args) {
		SpringApplication.run(HsCityApplication.class, args);
	}
}
