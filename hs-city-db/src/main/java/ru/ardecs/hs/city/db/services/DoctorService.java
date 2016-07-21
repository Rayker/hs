package ru.ardecs.hs.city.db.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.ardecs.hs.city.db.repositories.SpecialityRepository;
import ru.ardecs.hs.common.entities.Doctor;
import ru.ardecs.hs.common.entities.Hospital;
import ru.ardecs.hs.common.entities.shared.Speciality;
import ru.ardecs.hs.city.db.repositories.DoctorRepository;
import ru.ardecs.hs.city.db.repositories.HospitalRepository;

@Service
public class DoctorService {
	private static Logger logger = LoggerFactory.getLogger(DoctorService.class);

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private SpecialityRepository specialityRepository;

	@Autowired
	private HospitalRepository hospitalRepository;

	@org.springframework.transaction.annotation.Transactional
	public void createDoctor(Long doctorId, Long specialityId, Long hospitalId) {
		doctorRepository.save(new Doctor());
	}

	@org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRED)
	public void createDoctor(String doctorName, boolean rollback) {
		Doctor doctor = new Doctor();
		doctor.setFullname(doctorName);
		doctorRepository.save(doctor);
//		try {
		createHospital(doctorName + " hospital", rollback);
//		} catch (RuntimeException e) {
//			logger.error("createHospital exception", e);
//		}
//		if (rollback) throw new RuntimeException();
	}

	@org.springframework.transaction.annotation.Transactional(propagation = Propagation.NEVER)
	public void createHospital(String hospitalName, boolean rollback) {
		Hospital hospital = new Hospital();
		hospital.setName(hospitalName);
		hospitalRepository.save(hospital);
		if (rollback) throw new RuntimeException();
	}

	@org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public Speciality createSpeciality(String specialityName, long delay) throws InterruptedException {
		logger.debug("get specialities, transaction = {}", TransactionSynchronizationManager.isActualTransactionActive());
		Speciality speciality = new Speciality();
		speciality.setName(specialityName);
		speciality = specialityRepository.save(speciality);
		Thread.sleep(delay);
		return speciality;
	}

	@org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public Iterable<Speciality> getAllSpecialities() {
		logger.debug("get specialities, transaction = {}", TransactionSynchronizationManager.isActualTransactionActive());
		return specialityRepository.findAll();
	}

	@org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Iterable<Speciality> getAllSpecialitiesNow() {
		logger.debug("get specialities now, transaction = {}", TransactionSynchronizationManager.isActualTransactionActive());
		return specialityRepository.findAll();
	}
}
