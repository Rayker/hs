package ru.ardecs.hs.hsdb.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.hscommon.entities.Hospital;

public interface HospitalRepository extends CrudRepository<Hospital, Long> {
}
