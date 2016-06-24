package ru.ardecs.hs.hsdb.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import ru.ardecs.hs.hsdb.entities.ReservedTime;

import java.sql.Date;
import java.util.List;

public interface ReservedTimeRepository extends CrudRepository<ReservedTime, Long> {
	List<ReservedTime> findByJobIntervalDoctorIdAndDate(Long id, Date date);

	List<ReservedTime> findByDate(Date date);
}
