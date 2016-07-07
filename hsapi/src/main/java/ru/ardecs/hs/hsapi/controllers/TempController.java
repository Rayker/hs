package ru.ardecs.hs.hsapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: 7/7/16 remove
@RestController
public class TempController {

	private static Logger logger = LoggerFactory.getLogger(TempController.class);

	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	ConfigurableApplicationContext context;

	@RequestMapping(value = "/temp")
	public void temp() {
		logger.info("JmsMessagingTemplate creating");
		JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate(jmsTemplate);
		logger.info("Message building");
		Message<String> message = MessageBuilder.withPayload("temp-destination").build();
		logger.info("Message sending");
		jmsMessagingTemplate.send("temp-destination", message);
		logger.info("Message was successfully sent");
	}
}
