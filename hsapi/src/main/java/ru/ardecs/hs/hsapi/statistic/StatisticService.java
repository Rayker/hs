package ru.ardecs.hs.hsapi.statistic;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatisticService {

	@Scheduled(fixedRate = 30 * 1000)
	public void collectAndSendStatistic() {

	}
}
