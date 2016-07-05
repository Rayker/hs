package ru.ardecs.hs.hsclient.controllers;

import my.wsdl.SendCityStatisticRequest;
import my.wsdl.SendSpecialityStatisticRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

@Endpoint
public class SoapEndpoint {
	private static final String NAMESPACE_URI = "http://localhost:8080/wsdl/cityStatistic.wsdl";

	private static final Logger logger = LoggerFactory.getLogger(SoapEndpoint.class);

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendSpecialityStatisticRequest")
	public void sendSpecialityStatistic(@RequestPayload SendSpecialityStatisticRequest request) {
		logger.info("sendSpecialityStatistic: id = {}, visitsNumber = {}", request.getSpecialityStatistic().getId(), request.getSpecialityStatistic().getVisitsNumber());
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendCityStatisticRequest")
	public void sendSpecialityStatistic(@RequestPayload SendCityStatisticRequest request) {
		logger.info("sendSpecialityStatistic: speciality = {}, date = {}, cityId = {}", request.getSpecialityStatistic(), request.getDate(), request.getCityId());
	}
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "cityStatisticRequest")
////	@ResponsePayload
//	public void sendCityStatistic(@RequestPayload CityStatisticRequest cityStatisticRequest) {
//		logger.info("sendCityStatistic: cityId = {}, speciality = {};", cityStatisticRequest.getCityId(), cityStatisticRequest.getSpeciality1());
//	}
//
////	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "specialityStatistic")
////	@ResponsePayload
////	public SpecialityStatistic sendSpecialityStatistic(@RequestPayload SpecialityStatistic specialityStatistic) {
////		logger.info("sendSpecialityStatistic: id = {}, visitsNumber = {};", specialityStatistic.getId(), specialityStatistic.getVisitsNumber());
////		return specialityStatistic;
////	}
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "specialityStatistic")
//	public void sendSpecialityStatistic(@RequestPayload SpecialityStatistic specialityStatistic) {
//		logger.info("sendSpecialityStatistic: id = {}, visitsNumber = {};", specialityStatistic.getId(), specialityStatistic.getVisitsNumber());
//	}
}