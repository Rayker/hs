package ru.ardecs.hs.hsapi;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hsapi.clients.StatisticClient;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.util.Properties;

@Component
public class Beans {
	@Bean
	public RedisTemplate<String, CachedVisit> redisTemplate(RedisConnectionFactory rc) {
		final RedisTemplate<String, CachedVisit> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(rc);
		return redisTemplate;
	}

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("hello.wsdl");
		return marshaller;
	}

	@Bean
	public StatisticClient statisticClient(Jaxb2Marshaller marshaller) {
		StatisticClient client = new StatisticClient();
		client.setDefaultUri("http://localhost:8080/sendCityStatistic");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}
