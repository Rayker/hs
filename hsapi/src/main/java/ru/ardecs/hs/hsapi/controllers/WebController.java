package ru.ardecs.hs.hsapi.controllers;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsapi.Example;
import ru.ardecs.hs.hsapi.TemplateGenerator;
import ru.ardecs.hs.hsapi.bl.ScheduleManager;
import ru.ardecs.hs.hsapi.bl.TicketModel;
import ru.ardecs.hs.hsapi.bl.VisitModel;
import ru.ardecs.hs.hsapi.requestmodels.*;
import ru.ardecs.hs.hsdb.entities.ReservedTime;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class WebController {
	@Autowired
	Example example;

	@Autowired
	TemplateGenerator templateGenerator;

	@Autowired
	private ReservedTimeRepository reservedTimeRepository;

	@Autowired
	private SpecialityRepository specialityRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private ScheduleManager scheduleManager;

	@RequestMapping(value = "/specialities.html", method = RequestMethod.GET)
	public String specialities() throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialities", specialityRepository.findAll());
		return templateGenerator.generateHtml(map, "specialities.ftl");
	}

	@RequestMapping(value = "/hospitals.html", method = RequestMethod.POST, params = {"specialityId"})
	public String hospitals(HospitalsRequestModel requestModel) throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialityId", requestModel.getSpecialityId());
		map.put("hospitals", doctorRepository.queryHospitalsBySpecialityId(requestModel.getSpecialityId()));
		return templateGenerator.generateHtml(map, "hospitals.ftl");
	}

	@RequestMapping(value = "/doctors.html", method = RequestMethod.POST, params = {"specialityId", "hospitalId"})
	public String doctors(DoctorsRequestModel doctorsRequestModel) throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialityId", doctorsRequestModel.getSpecialityId());
		map.put("hospitalId", doctorsRequestModel.getHospitalId());
		map.put("doctors", doctorRepository.findBySpecialityIdAndHospitalId(doctorsRequestModel.getSpecialityId(), doctorsRequestModel.getHospitalId()));
		return templateGenerator.generateHtml(map, "doctors.ftl");
	}

	@RequestMapping(value = "/dates.html", params = "doctorId")
	public String choseDate(DatesRequestModel datesRequestModel) throws IOException, TemplateException {
		Calendar calendar = Calendar.getInstance();
		List<Date> dates = IntStream.range(0, 7)
				.mapToObj(i -> {
					calendar.add(Calendar.DATE, 1);
					return calendar.getTime();
				})
				.collect(Collectors.toList());

		Map<String, Object> map = new HashMap<>();
		map.put("dates", dates);
		map.put("doctorId", datesRequestModel.getDoctorId());
		return templateGenerator.generateHtml(map, "dates.ftl");
	}

	@RequestMapping(value = "/intervals.html", method = RequestMethod.POST, params = {"doctorId", "date"})
	public String times(IntervalsRequestModel intervalsRequestModel) throws IOException, TemplateException {
		Map<String, Object> model = new HashMap<>();
		model.put("date", intervalsRequestModel.getDate());
		List<VisitModel> temp = scheduleManager.getTimes(intervalsRequestModel.getDoctorId(), intervalsRequestModel.getDate());
		model.put("visits", temp);
		return templateGenerator.generateHtml(model, "visitTimes.ftl");
	}

	@RequestMapping(value = "/visits/new.html", method = RequestMethod.POST, params = {"date", "numberInInterval", "intervalId"})
	public String getVisitForm(VisitFormRequestModel visitFormRequestModel) throws IOException, TemplateException {
		return templateGenerator.generateHtml(new VisitModel(visitFormRequestModel.getNumberInInterval(), visitFormRequestModel.getIntervalId(), null, visitFormRequestModel.getDate(), false), "visitForm.ftl");
	}

	@RequestMapping(value = "/visits", method = RequestMethod.POST, params = {"date", "numberInInterval", "intervalId"})
	public void createVisit(VisitCreatingRequestModel visitCreatingRequestModel, HttpServletResponse response) throws IOException {
		ReservedTime savedVisit = reservedTimeRepository.save(new ReservedTime(visitCreatingRequestModel.getIntervalId(),
				visitCreatingRequestModel.getNumberInInterval(), visitCreatingRequestModel.getDate(),
				visitCreatingRequestModel.getVisitorName(), visitCreatingRequestModel.getVisitorBirthday()));
		response.sendRedirect("visits/" + savedVisit.getId() + "/ticket.html");
	}

	@RequestMapping(value = "visits/{reservedTimeId}/ticket.html", method = RequestMethod.GET)
	public String getTicket(@PathVariable Long reservedTimeId) throws IOException, TemplateException {
		TicketModel model = new TicketModel(reservedTimeRepository.findOne(reservedTimeId));
		return templateGenerator.generateHtml(model, "ticket.ftl");
	}

	// TEMP

	@RequestMapping(value = "visits/{reservedTimeId}/ticket/save/redis", method = RequestMethod.GET)
	public void saveToRedis(@PathVariable Long reservedTimeId, HttpServletRequest request) throws IOException, TemplateException {
		example.save(reservedTimeRepository.findOne(reservedTimeId), request.getSession().getId());
	}

	@RequestMapping(value = "visits/ticket/get/redis.html", method = RequestMethod.GET)
	public String getTicketFromRedis(HttpServletRequest request) throws IOException, TemplateException {
		TicketModel model = new TicketModel(example.getValue(request.getSession().getId()));
		return templateGenerator.generateHtml(model, "ticket.ftl");
	}

	@RequestMapping(value = "visits", method = RequestMethod.GET)
	public List<ReservedTime> getAll() {
		List<ReservedTime> allKeys = example.getAllKeys();
		return allKeys;
	}
}
