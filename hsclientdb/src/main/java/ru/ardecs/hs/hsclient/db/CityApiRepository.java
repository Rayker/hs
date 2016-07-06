package ru.ardecs.hs.hsclient.db;

import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.hsclient.db.entities.CityApi;

public interface CityApiRepository extends CrudRepository<CityApi, Long> {
}
