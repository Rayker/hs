package ru.ardecs.hs.hsapi.statistic;

import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

public interface StatisticsSender {
	void sendCityStatisticRequest(SendCityStatisticRequest cityStatistic);
}
