package ru.ardecs.hs.hsapi.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class StatisticsJmsSender {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsCollector.class);

	@Autowired
	private StatisticsCollector statisticsCollector;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Scheduled(cron = "${application.statisticsCollection.cron}")
	public void sendCityStatisticRequest() {

		SendCityStatisticRequest cityStatistic = statisticsCollector.collect();

		logger.info("sendCityStatistic: request");

		// Send a message
		MessageCreator messageCreator = new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("ping!" + System.currentTimeMillis());
			}
		};
		logger.info("Sending a new message.");
		jmsTemplate.send("temp-destination", messageCreator);

		logger.info("sendCityStatistic: success");
	}
}
