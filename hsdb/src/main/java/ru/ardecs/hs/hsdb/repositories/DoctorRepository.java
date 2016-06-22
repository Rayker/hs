package ru.ardecs.hs.hsdb.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.hsdb.entities.Doctor;

import java.util.List;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
	List<Doctor> findBySpecialityId(Long specialityId, Pageable pageable);
	List<Doctor> findBySpecialityIdAndHospitalId(Long specialityId, Long hospitalId, Pageable pageable);
//	List<Hospital> findHospitalBySpecialityId(Long specialityId, Pageable pageable);
}
