package ru.ardecs.hs.hsclient.controllers;

import my.wsdl.SendCityStatisticRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import ru.ardecs.hs.hsclient.db.CityStatisticRepository;
import ru.ardecs.hs.hsclient.db.entities.CityStatistic;

import java.sql.Date;
import java.util.stream.Collectors;

@Endpoint
public class SoapEndpoint {
	private static final String NAMESPACE_URI = "http://localhost:8080/wsdl/cityStatistic.wsdl";

	private static final Logger logger = LoggerFactory.getLogger(SoapEndpoint.class);

	@Autowired
	private CityStatisticRepository repository;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendCityStatisticRequest")
	public void sendSpecialityStatistic(@RequestPayload SendCityStatisticRequest request) {
		logger.info("sendSpecialityStatistic: speciality = {}, date = {}, cityId = {}", request.getSpecialityStatistic(), request.getDate(), request.getCityId());

		Date date = new Date(request.getDate().toGregorianCalendar().getTime().getTime());
		long cityId = request.getCityId().longValue();

		repository.save(request
				.getSpecialityStatistic()
				.stream()
				.map(s -> {
					CityStatistic statistic = new CityStatistic();
					statistic.setVisitsNumber(s.getVisitsNumber().intValue());
					statistic.getSpeciality().setId(s.getId().longValue());
					statistic.setDate(date);
					statistic.getCityApi().setId(cityId);
					return statistic;
				})
				.collect(Collectors.toList()));

		logger.info("sendSpecialityStatistic: statistic is successfully saved");
	}
}