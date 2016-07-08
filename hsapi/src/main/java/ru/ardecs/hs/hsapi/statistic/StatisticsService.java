package ru.ardecs.hs.hsapi.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

@Component
public class StatisticsService {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

	@Autowired
	private StatisticsCollector statisticsCollector;

	@Autowired
	@Qualifier("statisticsXmlJmsSender")
	private StatisticsSender statisticsSender;

	@Scheduled(cron = "${application.statisticsCollection.cron}")
	public void collectAndSendCityStatistics() {
		logger.info("collectAndSendCityStatistics(): statistics collection");
		SendCityStatisticRequest cityStatistic = statisticsCollector.collect();

		logger.info("collectAndSendCityStatistics(): statistics sending");
		statisticsSender.sendCityStatisticRequest(cityStatistic);
	}

}