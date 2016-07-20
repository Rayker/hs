package ru.ardecs.hs.hsclient.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.ardecs.hs.hsclient.db.entities.CityStatistic;
import ru.ardecs.hs.hsclient.db.entities.SummarySpecialityStatistic;
import ru.ardecs.hs.hsclient.db.repositories.CityStatisticRepository;
import ru.ardecs.hs.hsclient.db.repositories.SpecialityStatisticRepository;

@Service
public class StatisticsRepositoryWrapper {
	private static Logger logger = LoggerFactory.getLogger(StatisticsRepositoryWrapper.class);

	@Autowired
	private CityStatisticRepository repository;

	@Autowired
	private SpecialityStatisticRepository specialityStatisticRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void updateSpecialityStatistic(CityStatistic newStatistic) {
		logger.debug("update statistic: cityId = {}, specialityId = {}, visitorsNumber = {}, transaction = {}, level = {}",
				newStatistic.getCityApi().getId(),
				newStatistic.getSpeciality().getId(),
				newStatistic.getVisitsNumber(),
				TransactionSynchronizationManager.isActualTransactionActive(),
				TransactionSynchronizationManager.getCurrentTransactionIsolationLevel());

		long specialityId = newStatistic.getSpeciality().getId();

		SummarySpecialityStatistic specialityStatistic = getOrCreateSummarySpecialityStatistic(specialityId);

		longBl();

		int oldVisitorsNumber = specialityStatistic.getVisitorsNumber();
		int newVisitorsNumber = oldVisitorsNumber + newStatistic.getVisitsNumber();
		specialityStatistic.setVisitorsNumber(newVisitorsNumber);

		repository.save(newStatistic);
		specialityStatisticRepository.save(specialityStatistic);

		logger.debug("end of update statistic: SummarySpecialityStatisticId = {}, cityId = {}, specialityId = {}: {} + {} = {}",
				specialityStatistic.getId(),
				newStatistic.getCityApi().getId(),
				newStatistic.getSpeciality().getId(),
				oldVisitorsNumber,
				newStatistic.getVisitsNumber(),
				newVisitorsNumber);
	}

	private SummarySpecialityStatistic getOrCreateSummarySpecialityStatistic(long specialityId) {
		SummarySpecialityStatistic specialityStatistic = specialityStatisticRepository.findOne(specialityId);
		if (specialityStatistic == null) {
			specialityStatistic = new SummarySpecialityStatistic();
			specialityStatistic.getSpeciality().setId(specialityId);
		}
		return specialityStatistic;
	}

	private void longBl() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
			throw new RuntimeException(e);
		}
	}
}
