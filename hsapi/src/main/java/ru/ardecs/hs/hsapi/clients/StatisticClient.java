package ru.ardecs.hs.hsapi.clients;


import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import hello.wsdl.CityStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigInteger;
import java.util.GregorianCalendar;

public class StatisticClient extends WebServiceGatewaySupport {

	private static final Logger log = LoggerFactory.getLogger(StatisticClient.class);

	public CityStatistic sendCityStatistic() {
		CityStatistic statistic = new CityStatistic();
		statistic.setCityId(BigInteger.ONE);
//		statistic.setDate();
		CityStatistic.Speciality speciality = new CityStatistic.Speciality();
		speciality.setId(new BigInteger("70"));
		speciality.setVisitsNumber(new BigInteger("42"));
		statistic.getSpeciality().add(speciality);
		return statistic;
	}
}
