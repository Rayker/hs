package ru.ardecs.hs.central.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ardecs.hs.central.db.entities.CityStatistic;
import ru.ardecs.hs.common.soap.generated.SendCityStatisticRequest;

import java.sql.Date;

@Service
public class StatisticsService {
	private static Logger logger = LoggerFactory.getLogger(StatisticsService.class);
	@Autowired
	private StatisticsRepositoryWrapper statisticsRepositoryWrapper;

	public void save(SendCityStatisticRequest request) {
		Date date = new Date(request.getDate().toGregorianCalendar().getTime().getTime());
		long cityId = request.getCityId().longValue();

		logger.debug("handle request from city with cityId = {}", cityId);

		request.getSpecialityStatistic()
				.stream()
				.map(s -> {
					CityStatistic statistic = new CityStatistic();
					statistic.setVisitsNumber(s.getVisitsNumber().intValue());
					statistic.getSpeciality().setId(s.getId().longValue());
					statistic.setDate(date);
					statistic.getCityApi().setId(cityId);
					return statistic;
				})
				.parallel()
				.forEach(statisticsRepositoryWrapper::updateSpecialityStatistic);
	}

}
