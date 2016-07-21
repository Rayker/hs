package ru.ardecs.hs.hsdb.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ardecs.hs.common.entities.Speciality;

public interface SpecialityRepository extends PagingAndSortingRepository<Speciality, Long> {
}
