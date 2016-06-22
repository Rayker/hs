package ru.ardecs.hs.hsapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsdb.entities.Doctor;
import ru.ardecs.hs.hsdb.entities.Hospital;
import ru.ardecs.hs.hsdb.entities.Speciality;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.HospitalRepository;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApiController {
	@Autowired
	private SpecialityRepository specialityRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private HospitalRepository hospitalRepository;

	@RequestMapping(value = "/specialities", method = RequestMethod.GET)
	@ResponseBody
	public Iterable<Speciality> specialities(Pageable pageable) {
		return specialityRepository.findAll(pageable);
	}

	@RequestMapping(value = "/hospitals", method = RequestMethod.POST, params = {"specialityId"})
	@ResponseBody
	public List<Hospital> hospitals(Long specialityId, Pageable pageable) {
		return doctorRepository.findBySpecialityId(specialityId, pageable)
				.stream()
				.map(Doctor::getHospital)
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/doctors", method = RequestMethod.POST, params = {"specialityId", "hospitalId"})
	@ResponseBody
	public List<Doctor> doctors(Long specialityId, Long hospitalId, Pageable pageable) {
		return doctorRepository.findBySpecialityIdAndHospitalId(specialityId, hospitalId, pageable);
	}

//	@RequestMapping(value = "/temp", method = RequestMethod.GET)
//	@ResponseBody
//	public List<Hospital> temp(Long specialityId, Pageable pageable) {
//		List<Hospital> list = doctorRepository.findHospitalBySpecialityId(specialityId, pageable);
//		return list;
//	}


//	public Iterable<Hospital> hospitals(Speciality speciality) {
//		return hospitalRepository.find()
//	}
}