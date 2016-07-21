package ru.ardecs.hs.central.db.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.central.db.entities.CityApi;

public interface CityApiRepository extends CrudRepository<CityApi, Long> {
}
