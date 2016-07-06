package ru.ardecs.hs.hsapi.clients;


import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;
import ru.ardecs.hs.hscommon.soap.generated.SpecialityStatistic;

import java.math.BigInteger;

public class StatisticClient extends WebServiceGatewaySupport {

	private static final Logger logger = LoggerFactory.getLogger(StatisticClient.class);

	public void sendCityStatisticRequest() {
		logger.info("sendCityStatistic: start");

		SendCityStatisticRequest request = new SendCityStatisticRequest();
		request.setCityId(BigInteger.ONE);
		XMLGregorianCalendarImpl xmlGregorianCalendar = new XMLGregorianCalendarImpl();
		xmlGregorianCalendar.setYear(2016);
		xmlGregorianCalendar.setMonth(7);
		xmlGregorianCalendar.setDay(6);
		request.setDate(xmlGregorianCalendar);

		SpecialityStatistic specialityStatistic = new SpecialityStatistic();
		specialityStatistic.setId(BigInteger.ONE);
		specialityStatistic.setVisitsNumber(new BigInteger("42"));
		request.getSpecialityStatistic().add(specialityStatistic);
		logger.info("sendCityStatistic: request");

		getWebServiceTemplate().marshalSendAndReceive(request);

		logger.info("sendCityStatistic: success");
	}
}
