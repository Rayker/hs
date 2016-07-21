package ru.ardecs.hs.city.controllers;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.city.bl.ScheduleFactory;
import ru.ardecs.hs.city.bl.ScheduleManager;
import ru.ardecs.hs.city.cache.CachedVisit;
import ru.ardecs.hs.common.entities.Doctor;
import ru.ardecs.hs.common.entities.Hospital;
import ru.ardecs.hs.common.entities.ReservedTime;
import ru.ardecs.hs.common.entities.shared.Speciality;
import ru.ardecs.hs.common.models.TicketModel;
import ru.ardecs.hs.common.models.VisitModel;
import ru.ardecs.hs.common.requestmodels.*;
import ru.ardecs.hs.city.db.repositories.DoctorRepository;
import ru.ardecs.hs.city.db.repositories.ReservedTimeRepository;
import ru.ardecs.hs.city.db.repositories.SpecialityRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
public class ApiController {
	@Autowired
	private SpecialityRepository specialityRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private ReservedTimeRepository reservedTimeRepository;

	@Autowired
	private ScheduleManager scheduleManager;

	@Autowired
	private ScheduleFactory scheduleFactory;

	@RequestMapping(value = "/specialities.json", method = RequestMethod.GET)
	public Iterable<Speciality> specialities() {
		return specialityRepository.findAll();
	}

	@RequestMapping(value = "/hospitals.json", method = RequestMethod.GET)
	public List<Hospital> hospitals(@Validated HospitalsRequestModel requestModel) {
		return doctorRepository.queryHospitalsBySpecialityId(requestModel.getSpecialityId());
	}

	@RequestMapping(value = "/doctors.json", method = RequestMethod.GET, params = {"specialityId", "hospitalId"})
	public List<Doctor> doctors(@Validated DoctorsRequestModel doctorsRequestModel) {
		return doctorRepository.findBySpecialityIdAndHospitalId(doctorsRequestModel.getSpecialityId(), doctorsRequestModel.getHospitalId());
	}

	@RequestMapping(value = "/doctors/{doctorId}/workdays.json", method = RequestMethod.GET)
	public List<Date> choseDate(@PathVariable Long doctorId) throws IOException, TemplateException {
		// TODO: 6/30/16 refactor it
		return scheduleManager.getWorkDays(doctorId, 7);
	}

	@RequestMapping(value = "/visits/all.json", method = RequestMethod.GET, params = "sessionId")
	public List<VisitModel> times(@Validated IntervalsRequestModel intervalsRequestModel, String sessionId) {
		return scheduleManager.getVisitsByNotSessionId(
				intervalsRequestModel.getDoctorId(),
				intervalsRequestModel.getDate(),
				sessionId);
	}

	@RequestMapping(value = "/cache/visits", method = RequestMethod.POST, params = "sessionId")
	public void cache(@Validated VisitFormRequestModel visitFormRequestModel, String sessionId) {
		CachedVisit cachedVisit = new CachedVisit(
				doctorRepository.findOneByJobIntervalId(visitFormRequestModel.getJobIntervalId()).getId(),
				visitFormRequestModel.getJobIntervalId(),
				visitFormRequestModel.getNumberInInterval(),
				visitFormRequestModel.getDate());
		scheduleManager.cache(cachedVisit, sessionId);
	}

	@RequestMapping(value = "/visits", method = RequestMethod.POST, params = {"sessionId"})
	public long createVisit(@Validated VisitCreatingRequestModel visitCreatingRequestModel, String sessionId) throws IOException {
		ReservedTime reservedTime = new ReservedTime(
				visitCreatingRequestModel.getJobIntervalId(),
				visitCreatingRequestModel.getNumberInInterval(),
				visitCreatingRequestModel.getDate(),
				visitCreatingRequestModel.getVisitorName(),
				visitCreatingRequestModel.getVisitorBirthday());

		ReservedTime savedVisit = scheduleManager.save(reservedTime, sessionId);
		return savedVisit.getId();
	}

	@RequestMapping(value = "/visits/{reservedTimeId}/ticket.json", method = RequestMethod.GET)
	public TicketModel getReservedTime(@PathVariable Long reservedTimeId) throws IOException, TemplateException {
		return scheduleFactory.createTicketModel(reservedTimeRepository.findOne(reservedTimeId));
	}

	@RequestMapping(value = "/visits/{reservedTimeId}", method = RequestMethod.DELETE)
	public ResponseEntity delete(@PathVariable Long reservedTimeId) {
		try {
			reservedTimeRepository.delete(reservedTimeId);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}