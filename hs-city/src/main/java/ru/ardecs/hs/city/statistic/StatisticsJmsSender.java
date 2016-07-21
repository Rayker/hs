package ru.ardecs.hs.city.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.common.soap.generated.SendCityStatisticRequest;

@Component
public class StatisticsJmsSender implements StatisticsSender {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsJmsSender.class);

	@Autowired
	JmsMessagingTemplate jmsMessagingTemplate;

	@Value("${application.jms.destination}")
	private String destination;

	@Override
	public void sendCityStatisticRequest(SendCityStatisticRequest cityStatistic) {
		logger.debug("sendCityStatistic(): sending message");
		jmsMessagingTemplate.convertAndSend(destination, cityStatistic);

		logger.debug("sendCityStatistic(): success");
	}
}
