package ru.ardecs.hs.hsdb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.hsdb.entities.Speciality;

import java.util.List;

public interface SpecialityRepository extends CrudRepository<Speciality, Long> {
	Page<Speciality> findAll(Pageable pageable);
}
