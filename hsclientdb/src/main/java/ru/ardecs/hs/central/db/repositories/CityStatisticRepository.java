package ru.ardecs.hs.central.db.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.central.db.entities.CityStatistic;

public interface CityStatisticRepository extends CrudRepository<CityStatistic, Long> {
}
