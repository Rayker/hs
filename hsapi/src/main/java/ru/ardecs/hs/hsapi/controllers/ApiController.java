package ru.ardecs.hs.hsapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsdb.entities.Doctor;
import ru.ardecs.hs.hsdb.entities.Hospital;
import ru.ardecs.hs.hsdb.entities.Speciality;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.HospitalRepository;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;

import java.util.List;

@RestController
public class ApiController {
	@Autowired
	private SpecialityRepository specialityRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private HospitalRepository hospitalRepository;

	@RequestMapping(value = "/specialities.json", method = RequestMethod.GET)
	public Page<Speciality> specialities(Pageable pageable) {
		return specialityRepository.findAll(pageable);
	}

	@RequestMapping(value = "/hospitals.json", method = RequestMethod.POST, params = {"specialityId"})
	public List<Hospital> hospitals(Long specialityId, Pageable pageable) {
		return doctorRepository.queryHospitalsBySpecialityId(specialityId, pageable);
	}

	@RequestMapping(value = "/doctors.json", method = RequestMethod.POST, params = {"specialityId", "hospitalId"})
	public List<Doctor> doctors(Long specialityId, Long hospitalId, Pageable pageable) {
		return doctorRepository.findBySpecialityIdAndHospitalId(specialityId, hospitalId, pageable);
	}
}