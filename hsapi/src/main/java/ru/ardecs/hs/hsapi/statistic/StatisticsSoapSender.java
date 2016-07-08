package ru.ardecs.hs.hsapi.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

public class StatisticsSoapSender extends WebServiceGatewaySupport implements StatisticsSender {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsSoapSender.class);

	public void sendCityStatisticRequest(SendCityStatisticRequest cityStatistic) {
		logger.info("sendCityStatistic(): sending");
		getWebServiceTemplate().marshalSendAndReceive(cityStatistic);

		logger.info("sendCityStatistic(): success");
	}
}
