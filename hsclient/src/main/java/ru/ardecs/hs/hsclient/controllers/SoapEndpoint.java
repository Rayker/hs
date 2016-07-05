package ru.ardecs.hs.hsclient.controllers;

import my.wsdl.GetCountryRequest;
import my.wsdl.GetCountryResponse;
import my.wsdl.SendSpecialityStatisticRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SoapEndpoint {
	private static final String NAMESPACE_URI = "http://localhost:8080/wsdl/cityStatistic.wsdl";

	private static final Logger logger = LoggerFactory.getLogger(SoapEndpoint.class);

	private CountryRepository countryRepository;

	@Autowired
	public SoapEndpoint(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
	@ResponsePayload
	public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
		GetCountryResponse response = new GetCountryResponse();
		response.setCountry(countryRepository.findCountry(request.getName()));

		return response;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendSpecialityStatisticRequest")
	@ResponsePayload
	public GetCountryResponse sendSpecialityStatistic(@RequestPayload SendSpecialityStatisticRequest request) {
		logger.info("sendSpecialityStatistic: id = {}, visitsNumber = {}", request.getSpecialityStatistic().getId(), request.getSpecialityStatistic().getVisitsNumber());
		GetCountryResponse response = new GetCountryResponse();
		response.setCountry(countryRepository.findCountry("Poland"));
		return response;
	}

//	private static final String NAMESPACE_URI = "http://localhost:8080/wsdl/cityStatistic.wsdl";
//
//
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