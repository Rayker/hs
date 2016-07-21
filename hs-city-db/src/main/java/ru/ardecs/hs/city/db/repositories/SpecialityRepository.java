package ru.ardecs.hs.city.db.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.ardecs.hs.common.entities.shared.Speciality;

public interface SpecialityRepository extends PagingAndSortingRepository<Speciality, Long> {
}
