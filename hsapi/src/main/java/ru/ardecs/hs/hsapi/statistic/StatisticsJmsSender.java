package ru.ardecs.hs.hsapi.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

@Component
public class StatisticsJmsSender {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsJmsSender.class);

	@Autowired
	JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private StatisticsCollector statisticsCollector;

	@Value("${application.jms.destination}")
	private String destination;

	@Scheduled(cron = "${application.statisticsCollection.cron}")
	public void sendCityStatisticRequest() {

		logger.info("sendCityStatistic(): statistics collection");
		SendCityStatisticRequest cityStatistic = statisticsCollector.collect();

		logger.info("sendCityStatistic(): creating message");
		org.springframework.messaging.Message<SendCityStatisticRequest> message =
				MessageBuilder.withPayload(cityStatistic).build();

		logger.info("sendCityStatistic(): sending message");
		jmsMessagingTemplate.send(destination, message);

		logger.info("sendCityStatistic(): success");
	}
}
