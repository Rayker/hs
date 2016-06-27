package ru.ardecs.hs.hsdb.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.hsdb.entities.Doctor;
import ru.ardecs.hs.hsdb.entities.Hospital;

import java.util.List;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
	@Query("select distinct d.hospital from Doctor d where d.speciality.id = ?1")
	List<Hospital> queryHospitalsBySpecialityId(Long specialityId);
	List<Doctor> findBySpecialityIdAndHospitalId(Long specialityId, Long hospitalId);
}
