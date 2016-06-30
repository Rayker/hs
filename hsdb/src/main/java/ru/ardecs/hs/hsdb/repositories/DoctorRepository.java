package ru.ardecs.hs.hsdb.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.hscommon.entities.Doctor;
import ru.ardecs.hs.hscommon.entities.Hospital;

import java.util.List;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
	@Query("select distinct d.hospital from Doctor d where d.speciality.id = ?1")
	List<Hospital> queryHospitalsBySpecialityId(Long specialityId);

	List<Doctor> findBySpecialityIdAndHospitalId(Long specialityId, Long hospitalId);

	@Query("select j.doctor from JobInterval j where j.id = ?1")
	Doctor findOneByJobIntervalId(Long jobIntervalId);
}
