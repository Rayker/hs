package ru.ardecs.hs.hsclient.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ardecs.hs.hsclient.db.entities.CityStatistic;
import ru.ardecs.hs.hsclient.db.entities.SummarySpecialityStatistic;
import ru.ardecs.hs.hsclient.db.repositories.CityStatisticRepository;
import ru.ardecs.hs.hsclient.db.repositories.SpecialityStatisticRepository;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;
import ru.ardecs.hs.hscommon.soap.generated.SpecialityStatistic;

import java.sql.Date;
import java.util.stream.Collectors;

@Service
public class StatisticsRepositoryWrapper {
	private static Logger logger = LoggerFactory.getLogger(StatisticsRepositoryWrapper.class);

	@Autowired
	private CityStatisticRepository repository;

	@Autowired
	private SpecialityStatisticRepository specialityStatisticRepository;

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
				.parallel().forEach(this::updateSpecialityStatistic);
	}

	@Transactional
	private void updateSpecialityStatistic(CityStatistic newStatistic) {
		logger.debug("update statistic: cityId = {}, specialityId = {}", newStatistic.getCityApi().getId(), newStatistic.getSpeciality().getId());
		long specialityId = newStatistic.getSpeciality().getId();
		SummarySpecialityStatistic specialityStatistic = specialityStatisticRepository.findOne(specialityId);

		if (specialityStatistic == null) {
			specialityStatistic = new SummarySpecialityStatistic();
			specialityStatistic.getSpeciality().setId(specialityId);
		}

		int newVisitorsNumber = specialityStatistic.getVisitorsNumber() + newStatistic.getVisitsNumber();
		specialityStatistic.setVisitorsNumber(newVisitorsNumber);
		repository.save(newStatistic);
		specialityStatisticRepository.save(specialityStatistic);
	}
}
