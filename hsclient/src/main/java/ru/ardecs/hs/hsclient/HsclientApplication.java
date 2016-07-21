package ru.ardecs.hs.hsclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@EnableWs
@EnableWebMvc
@EnableJms
@EnableScheduling
@EnableTransactionManagement
@ComponentScan(basePackages = {"ru.ardecs.hs.hsclient.*", "ru.ardecs.hs.*"})
@EntityScan(basePackages = {"ru.ardecs.hs.hsclient.db.entities", "ru.ardecs.hs.common.entities.shared"})
public class HsclientApplication {
	public static void main(String[] args) {
		SpringApplication.run(HsclientApplication.class, args);
	}
}
