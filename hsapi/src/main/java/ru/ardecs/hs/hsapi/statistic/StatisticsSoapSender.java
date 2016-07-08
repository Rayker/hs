package ru.ardecs.hs.hsapi.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

public class StatisticsSoapSender extends WebServiceGatewaySupport {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsSoapSender.class);

	@Autowired
	private StatisticsCollector statisticsCollector;

//	@Scheduled(cron = "${application.statisticsCollection.cron}")
	public void sendCityStatisticRequest() {
		logger.info("sendCityStatistic(): statistics collection");

		SendCityStatisticRequest cityStatistic = statisticsCollector.collect();

		logger.info("sendCityStatistic(): request");
		getWebServiceTemplate().marshalSendAndReceive(cityStatistic);

		logger.info("sendCityStatistic(): success");
	}
}
