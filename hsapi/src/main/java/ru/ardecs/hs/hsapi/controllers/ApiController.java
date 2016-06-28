package ru.ardecs.hs.hsapi.controllers;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsapi.bl.ScheduleManager;
import ru.ardecs.hs.hsapi.bl.VisitModel;
import ru.ardecs.hs.hsapi.mail.MailSender;
import ru.ardecs.hs.hsdb.entities.Doctor;
import ru.ardecs.hs.hsdb.entities.Hospital;
import ru.ardecs.hs.hsdb.entities.Speciality;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;

import javax.servlet.http.HttpServletResponse;
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
	private MailSender mailSender;

	@RequestMapping(value = "/specialities.json", method = RequestMethod.GET)
	public Page<Speciality> specialities(Pageable pageable) {
		return specialityRepository.findAll(pageable);
	}

	@RequestMapping(value = "/hospitals.json", method = RequestMethod.GET, params = {"specialityId"})
	public List<Hospital> hospitals(Long specialityId, Pageable pageable) {
		return doctorRepository.queryHospitalsBySpecialityId(specialityId);
	}

	@RequestMapping(value = "/doctors.json", method = RequestMethod.GET, params = {"specialityId", "hospitalId"})
	public List<Doctor> doctors(Long specialityId, Long hospitalId, Pageable pageable) {
		return doctorRepository.findBySpecialityIdAndHospitalId(specialityId, hospitalId);
	}

	@RequestMapping(value = "/intervals.json", method = RequestMethod.GET, params = {"doctorId", "date"})
	public List<VisitModel> times(Long doctorId, @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		return scheduleManager.getTimes(doctorId, date);
	}

	@RequestMapping(value = "/visits/{reservedTimeId}", method = RequestMethod.DELETE)
	public ResponseEntity getVisitModel(@PathVariable Long reservedTimeId) {
		try {
			reservedTimeRepository.delete(reservedTimeId);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/visits/{reservedTimeId}/ticket/send", method = RequestMethod.POST, params = {"addressTo"})
	public void send(@PathVariable Long reservedTimeId, String addressTo, HttpServletResponse response) throws IOException, TemplateException {
		mailSender.send(addressTo, reservedTimeId);
		response.sendRedirect("/visits/" + reservedTimeId + "/ticket.html");
	}
}