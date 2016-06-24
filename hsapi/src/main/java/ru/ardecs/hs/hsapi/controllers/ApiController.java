package ru.ardecs.hs.hsapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.ardecs.hs.hsapi.bl.ScheduleManager;
import ru.ardecs.hs.hsapi.bl.VisitModel;
import ru.ardecs.hs.hsdb.entities.*;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.PostConstruct;
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

	private ScheduleManager scheduleManager;

	@RequestMapping(value = "/specialities.json", method = RequestMethod.GET)
	public Page<Speciality> specialities(Pageable pageable) {
		return specialityRepository.findAll(pageable);
	}

	@RequestMapping(value = "/hospitals.json", method = RequestMethod.GET, params = {"specialityId"})
	public List<Hospital> hospitals(Long specialityId, Pageable pageable) {
		return doctorRepository.queryHospitalsBySpecialityId(specialityId, pageable);
	}

	@RequestMapping(value = "/doctors.json", method = RequestMethod.GET, params = {"specialityId", "hospitalId"})
	public List<Doctor> doctors(Long specialityId, Long hospitalId, Pageable pageable) {
		return doctorRepository.findBySpecialityIdAndHospitalId(specialityId, hospitalId, pageable);
	}

	@RequestMapping(value = "/intervals.json", method = RequestMethod.GET, params = {"doctorId", "date"})
	public List<VisitModel> times(Long doctorId, java.sql.Date date) {
		return scheduleManager.getTimes(doctorId, date);
	}

	@RequestMapping(value = "/visits", method = RequestMethod.POST, params = {"jobIntervalId", "numberInInterval", "date"})
	public Long reserveVisit(Long jobIntervalId, int numberInInterval, java.sql.Date date) {
		// todo check
		ReservedTime saved = reservedTimeRepository.save(new ReservedTime(jobIntervalId, numberInInterval, date));
		long id = saved.getId();
		return id;
	}

//	@RequestMapping(value = "/visits/${reservedTimeId}", method = RequestMethod.GET)
//	public VisitModel getVisitModel(Long reservedTimeId) {
//		reservedTimeRepository.findOne(reservedTimeId);
//	}

	@RequestMapping(value = "/visits/{reservedTimeId}", method = RequestMethod.DELETE)
	public void getVisitModel(@PathVariable Long reservedTimeId) {
		reservedTimeRepository.delete(reservedTimeId);
	}
}