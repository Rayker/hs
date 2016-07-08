package ru.ardecs.hs.hsapi;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hsapi.statistic.StatisticsSoapSender;

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
	public StatisticsSoapSender statisticsSoapSender(Jaxb2Marshaller marshaller) {
		StatisticsSoapSender sender = new StatisticsSoapSender();
		sender.setDefaultUri("http://localhost:8080/ws");
		sender.setMarshaller(marshaller);
		sender.setUnmarshaller(marshaller);
		return sender;
	}

	@Bean
	public DatatypeFactory datatypeFactory() throws DatatypeConfigurationException {
		return DatatypeFactory.newInstance();
	}

	@Bean
	public JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
		return new JmsMessagingTemplate(jmsTemplate);
	}
}
