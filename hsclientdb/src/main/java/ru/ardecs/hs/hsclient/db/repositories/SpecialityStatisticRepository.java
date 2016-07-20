package ru.ardecs.hs.hsclient.db.repositories;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.Repository;
import ru.ardecs.hs.hsclient.db.entities.SummarySpecialityStatistic;

import javax.persistence.LockModeType;

public interface SpecialityStatisticRepository extends Repository<SummarySpecialityStatistic, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	SummarySpecialityStatistic findOne(Long id);

	SummarySpecialityStatistic save(SummarySpecialityStatistic summarySpecialityStatistic);

}
