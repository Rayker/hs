package ru.ardecs.hs.hsclient.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import ru.ardecs.hs.hsclient.db.entities.CityStatistic;
import ru.ardecs.hs.hsclient.db.repositories.CityStatisticRepository;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

import java.sql.Date;
import java.util.stream.Collectors;

@Component
public class StatisticsRepositoryWrapper {
	@Autowired
	private CityStatisticRepository repository;

	public void save(@RequestPayload SendCityStatisticRequest request) {
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
	}

}
