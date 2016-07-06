package ru.ardecs.hs.hsclient.db;

import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.hsclient.db.entities.CityStatistic;

public interface CityStatisticRepository extends CrudRepository<CityStatistic, Long> {
}
