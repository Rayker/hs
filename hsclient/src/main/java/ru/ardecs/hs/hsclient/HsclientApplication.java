package ru.ardecs.hs.hsclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableWs
@EnableJms
@EnableScheduling
@ComponentScan(basePackages = {"ru.ardecs.hs.hsclient.*", "ru.ardecs.hs.*"})
@EntityScan(basePackages = {"ru.ardecs.hs.hsclient.*", "ru.ardecs.hs.hscommon.*"})
public class HsclientApplication {
	public static void main(String[] args) {
		SpringApplication.run(HsclientApplication.class, args);
	}
}
