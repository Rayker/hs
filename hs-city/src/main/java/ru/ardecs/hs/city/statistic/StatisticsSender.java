package ru.ardecs.hs.city.statistic;

import ru.ardecs.hs.common.soap.generated.SendCityStatisticRequest;

public interface StatisticsSender {
	void sendCityStatisticRequest(SendCityStatisticRequest cityStatistic);
}
