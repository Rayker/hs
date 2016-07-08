package ru.ardecs.hs.hsclient.soap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import ru.ardecs.hs.hsclient.db.entities.CityStatistic;
import ru.ardecs.hs.hsclient.db.repositories.CityStatisticRepository;
import ru.ardecs.hs.hsclient.statistic.StatisticsRepositoryWrapper;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

import java.sql.Date;
import java.util.stream.Collectors;

//@Endpoint
public class SoapEndpoint {
	private static final String NAMESPACE_URI = "http://localhost:8080/wsdl/cityStatistic.wsdl";

	private static final Logger logger = LoggerFactory.getLogger(SoapEndpoint.class);

	@Autowired
	private StatisticsRepositoryWrapper repositoryWrapper;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendCityStatisticRequest")
	public void sendSpecialityStatistic(@RequestPayload SendCityStatisticRequest request) {
		logger.info("sendSpecialityStatistic: date = {}, cityId = {}", request.getDate(), request.getCityId());

		repositoryWrapper.save(request);

		logger.info("sendSpecialityStatistic: statistic is successfully saved");
	}
}