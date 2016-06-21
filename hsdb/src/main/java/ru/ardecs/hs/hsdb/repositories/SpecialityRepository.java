package ru.ardecs.hs.hsdb.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.hsdb.entities.Speciality;

public interface SpecialityRepository extends CrudRepository<Speciality, Long> {
}
