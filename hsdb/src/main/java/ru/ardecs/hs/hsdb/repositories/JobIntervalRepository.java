package ru.ardecs.hs.hsdb.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import ru.ardecs.hs.hsdb.entities.JobInterval;

public interface JobIntervalRepository extends CrudRepository<JobInterval, Long> {
}
