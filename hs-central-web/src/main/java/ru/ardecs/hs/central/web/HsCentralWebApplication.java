package ru.ardecs.hs.central.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableWs
//@EnableWebMvc
@ComponentScan(basePackages = {"ru.ardecs.hs.central.*", "ru.ardecs.hs.*"})
public class HsCentralWebApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(HsCentralWebApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(HsCentralWebApplication.class);
	}
}
