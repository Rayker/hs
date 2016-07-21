package ru.ardecs.hs.hsapi.statistic;

import ru.ardecs.hs.common.soap.generated.SendCityStatisticRequest;

public interface StatisticsSender {
	void sendCityStatisticRequest(SendCityStatisticRequest cityStatistic);
}
