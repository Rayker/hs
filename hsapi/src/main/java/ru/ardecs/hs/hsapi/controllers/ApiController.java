package ru.ardecs.hs.hsapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsapi.bl.ScheduleManager;
import ru.ardecs.hs.hsapi.bl.VisitModel;
import ru.ardecs.hs.hsdb.entities.*;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.JobIntervalRepository;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@RestController
public class ApiController {
	@PostConstruct
	public void init() {
		scheduleManager = new ScheduleManager(reservedTimeRepository, doctorRepository);
	}

	@Autowired
	private SpecialityRepository specialityRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private ReservedTimeRepository reservedTimeRepository;
	@Autowired
	private JobIntervalRepository jobIntervalRepository;

	private ScheduleManager scheduleManager;

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

	@RequestMapping(value = "/intervals.json", method = RequestMethod.POST, params = {"doctorId", "date"})
	public List<VisitModel> times(Long doctorId, java.sql.Date date) {
		List<VisitModel> times = scheduleManager.getTimes(doctorId, date);
		return times;
	}
}