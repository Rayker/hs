package ru.ardecs.hs.hsclient.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class StatisticReceiver {
	private static Logger logger = LoggerFactory.getLogger(StatisticReceiver.class);

	@JmsListener(destination = "temp-destination")
	public void receiveMessage(String message) {
		logger.info("Received message: {}", message);
	}

}
