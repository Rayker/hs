package ru.ardecs.hs.hsclient.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

@Component
public class StatisticReceiver {
	private static Logger logger = LoggerFactory.getLogger(StatisticReceiver.class);

	@Autowired
	private StatisticsRepositoryWrapper repositoryWrapper;

	@JmsListener(destination = "${application.jms.destination}")
	public void receiveMessage(SendCityStatisticRequest request) {
		logger.debug("sendSpecialityStatistic: date = {}, cityId = {}", request.getDate(), request.getCityId());
		repositoryWrapper.save(request);
		logger.debug("sendSpecialityStatistic: statistic is successfully saved");
	}

}
