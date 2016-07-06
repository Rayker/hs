package ru.ardecs.hs.hsapi;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hsapi.statistic.soap.StatisticClient;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

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
		marshaller.setContextPath("ru.ardecs.hs.hscommon.soap.generated");
		return marshaller;
	}

	@Bean
	public StatisticClient statisticClient(Jaxb2Marshaller marshaller) {
		StatisticClient client = new StatisticClient();
		client.setDefaultUri("http://localhost:8080/ws");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

	@Bean
	public DatatypeFactory datatypeFactory() throws DatatypeConfigurationException {
		return DatatypeFactory.newInstance();
	}
}
