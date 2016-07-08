package ru.ardecs.hs.hsapi.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;
import ru.ardecs.hs.hscommon.soap.generated.SpecialityStatistic;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;

import javax.xml.datatype.DatatypeFactory;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
public class StatisticsCollector {
	private static final Logger logger = LoggerFactory.getLogger(StatisticsCollector.class);

	@Autowired
	private DoctorRepository repository;

	@Autowired
	private DatatypeFactory datatypeFactory;

	@Value("${application.city.id}")
	private long cityId;

	public SendCityStatisticRequest collect() {
		logger.info("collect(): start");

		SendCityStatisticRequest request = new SendCityStatisticRequest();
		request.setCityId(BigInteger.valueOf(cityId));
		request.setDate(datatypeFactory.newXMLGregorianCalendar(new GregorianCalendar()));

		logger.info("collect(): statistics collection");
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

		logger.info("collect(): statistics was collected");
		return request;
	}
}
