package ru.ardecs.hs.hsdb.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.hscommon.entities.ReservedTime;

import java.sql.Date;
import java.util.List;

public interface ReservedTimeRepository extends CrudRepository<ReservedTime, Long> {
	List<ReservedTime> findByJobIntervalDoctorIdAndDate(Long doctorId, Date date);
}
