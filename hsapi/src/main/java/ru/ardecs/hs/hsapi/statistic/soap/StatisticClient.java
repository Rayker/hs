package ru.ardecs.hs.hsapi.statistic.soap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;
import ru.ardecs.hs.hscommon.soap.generated.SpecialityStatistic;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;

import javax.xml.datatype.DatatypeFactory;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;

public class StatisticClient extends WebServiceGatewaySupport {

	private static final Logger logger = LoggerFactory.getLogger(StatisticClient.class);

	@Autowired
	private DoctorRepository repository;

	@Autowired
	private DatatypeFactory datatypeFactory;

	@Value("${application.city.id}")
	private long cityId;

	@Scheduled(cron = "${application.statisticsCollection.cron}")
	public void sendCityStatisticRequest() {
		logger.info("sendCityStatistic: start");

		SendCityStatisticRequest request = new SendCityStatisticRequest();
		request.setCityId(BigInteger.valueOf(cityId));
		request.setDate(datatypeFactory.newXMLGregorianCalendar(new GregorianCalendar()));

		logger.info("sendCityStatistic: statistics collection");
		repository
				.findBySpeciality(new java.sql.Date(new Date().getTime()))
				.stream()
				.map(e -> {
					SpecialityStatistic s = new SpecialityStatistic();
					s.setVisitsNumber(BigInteger.valueOf((Long) e[0]));
					s.setId(BigInteger.valueOf((Long) e[1]));
					return s;
				})
				.forEach(s -> request.getSpecialityStatistic().add(s));

		logger.info("sendCityStatistic: request");
		getWebServiceTemplate().marshalSendAndReceive(request);

		logger.info("sendCityStatistic: success");
	}
}
