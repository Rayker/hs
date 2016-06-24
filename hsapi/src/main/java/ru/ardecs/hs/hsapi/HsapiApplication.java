package ru.ardecs.hs.hsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"ru.ardecs.hs.hsdb.repositories"})
@EntityScan("ru.ardecs.hs.hsdb.*")
@ComponentScan("ru.ardecs.hs.hsapi.*")
public class HsapiApplication {
	public static void main(String[] args) {
		SpringApplication.run(HsapiApplication.class, args);
	}
}
