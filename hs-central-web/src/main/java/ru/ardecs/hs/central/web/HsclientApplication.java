package ru.ardecs.hs.central.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.ardecs.hs.central.db.repositories.CityApiRepository;

@SpringBootApplication
//@EnableWs
@EnableWebMvc
@EnableScheduling
@ComponentScan(basePackages = {"ru.ardecs.hs.central.*", "ru.ardecs.hs.*", "ru.ardecs.hs.central.controllers", "ru.ardecs.hs.central.db.repositories"})
public class HsclientApplication {
	public static void main(String[] args) {
		SpringApplication.run(HsclientApplication.class, args);
	}
}
