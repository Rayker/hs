package ru.ardecs.hs.central.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@EnableWs
@EnableWebMvc
@ComponentScan(basePackages = {"ru.ardecs.hs.central.*", "ru.ardecs.hs.*"})
public class HsclientApplication {
	public static void main(String[] args) {
		SpringApplication.run(HsclientApplication.class, args);
	}
}
