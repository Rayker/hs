package ru.ardecs.hs.hsapi.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import static org.mockito.Mockito.mock;

@Configuration
public class Mocks {
	@Bean
	public JmsMessagingTemplate jmsMessagingTemplate() {
		return mock(JmsMessagingTemplate.class);
	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		return mock(Jaxb2Marshaller.class);
	}
}
