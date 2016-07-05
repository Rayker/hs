package ru.ardecs.hs.hsclient.controllers;

import my.wsdl.CityStatistic1;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

@Endpoint
public class SoapEndpoint {
	private static final String NAMESPACE_URI = "http://localhost:8080/wsdl/cityStatistic.wsdl";


	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "cityStatistic1")
//	@ResponsePayload
	public void sendCityStatistic(@RequestPayload CityStatistic1 cityStatistic1) {

	}
}