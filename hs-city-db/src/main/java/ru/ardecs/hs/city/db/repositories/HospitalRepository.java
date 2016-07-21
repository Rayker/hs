package ru.ardecs.hs.city.db.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.common.entities.Hospital;

public interface HospitalRepository extends CrudRepository<Hospital, Long> {
}
