package ru.ardecs.hs.hsapi.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import ru.ardecs.hs.hsapi.HsapiApplication;
import ru.ardecs.hs.hsapi.bl.ScheduleFactory;
import ru.ardecs.hs.hsapi.cache.MemoryCacheManager;

@Configuration
@ComponentScan(basePackages = {"ru.ardecs.hs.hsapi.cache", "ru.ardecs.hs.hsapi.bl"})
public class TestBeans {
	@Bean
	PropertyPlaceholderConfigurer propConfig() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setLocation(new ClassPathResource("application.properties"));
		return ppc;
	}


	@Bean
	public ScheduleFactory scheduleFactory(@Value("${application.schedule.visitInMinutes}") int visitInMinutes,
	                                       @Value("${application.schedule.timeFormatPattern}") String timeFormatPattern) {
		int visitItMilliseconds = visitInMinutes * 60 * 1000;
		return new ScheduleFactory(visitItMilliseconds, timeFormatPattern);
	}

	@Bean
	public MemoryCacheManager memoryCacheManager(@Value("${application.cache.expireTimeInMinutes}") int expireTimeInMinutes) {
		return new MemoryCacheManager(expireTimeInMinutes);
	}
}
