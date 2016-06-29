package ru.ardecs.hs.hsapi.controllers;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsapi.cache.CacheManager;
import ru.ardecs.hs.hsapi.TemplateGenerator;
import ru.ardecs.hs.hsapi.bl.ScheduleManager;
import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hsapi.models.TicketModel;
import ru.ardecs.hs.hsapi.models.VisitModel;
import ru.ardecs.hs.hsapi.requestmodels.*;
import ru.ardecs.hs.hsdb.entities.ReservedTime;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class WebController {
	@Autowired
	CacheManager cacheManager;

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

	@RequestMapping(value = "/hospitals.html", method = RequestMethod.GET, params = {"specialityId"})
	public String hospitals(HospitalsRequestModel requestModel) throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialityId", requestModel.getSpecialityId());
		map.put("hospitals", doctorRepository.queryHospitalsBySpecialityId(requestModel.getSpecialityId()));
		return templateGenerator.generateHtml(map, "hospitals.ftl");
	}

	@RequestMapping(value = "/doctors.html", method = RequestMethod.GET, params = {"specialityId", "hospitalId"})
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

	@RequestMapping(value = "/intervals.html", method = RequestMethod.GET, params = {"doctorId", "date"})
	public String times(IntervalsRequestModel intervalsRequestModel, HttpServletRequest request) throws IOException, TemplateException {
		Map<String, Object> model = new HashMap<>();
		model.put("date", intervalsRequestModel.getDate());
		List<VisitModel> temp = scheduleManager.getVisitsByNotSessionId(
				intervalsRequestModel.getDoctorId(),
				intervalsRequestModel.getDate(),
				request.getSession().getId());
		model.put("visits", temp);
		return templateGenerator.generateHtml(model, "visitTimes.ftl");
	}

	@RequestMapping(value = "/visits/new.html", method = RequestMethod.POST, params = {"date", "numberInInterval", "jobIntervalId"})
	public String getVisitForm(VisitFormRequestModel visitFormRequestModel, HttpSession session) throws IOException, TemplateException {
		CachedVisit cachedVisit = new CachedVisit(
				doctorRepository.findOneByJobIntervalId(visitFormRequestModel.getJobIntervalId()).getId(),
				visitFormRequestModel.getJobIntervalId(),
				visitFormRequestModel.getNumberInInterval(),
				visitFormRequestModel.getDate());
		cacheManager.cache(cachedVisit, session.getId());

		VisitModel visitModel = new VisitModel(
				visitFormRequestModel.getNumberInInterval(),
				visitFormRequestModel.getJobIntervalId(),
				null,
				visitFormRequestModel.getDate(),
				false);
		return templateGenerator.generateHtml(visitModel, "visitForm.ftl");
	}

	@RequestMapping(value = "/visits", method = RequestMethod.POST, params = {"date", "numberInInterval", "jobIntervalId"})
	public void createVisit(VisitCreatingRequestModel visitCreatingRequestModel, HttpServletResponse response, HttpSession session) throws IOException {
		ReservedTime reservedTime = new ReservedTime(
				visitCreatingRequestModel.getJobIntervalId(),
				visitCreatingRequestModel.getNumberInInterval(),
				visitCreatingRequestModel.getDate(),
				visitCreatingRequestModel.getVisitorName(),
				visitCreatingRequestModel.getVisitorBirthday());

		ReservedTime savedVisit = scheduleManager.save(reservedTime, session.getId());
		response.sendRedirect("visits/" + savedVisit.getId() + "/ticket.html");
	}

	@RequestMapping(value = "visits/{reservedTimeId}/ticket.html", method = RequestMethod.GET)
	public String getTicket(@PathVariable Long reservedTimeId) throws IOException, TemplateException {
		TicketModel model = new TicketModel(reservedTimeRepository.findOne(reservedTimeId));
		return templateGenerator.generateHtml(model, "ticket.ftl");
	}

	// TEMP

//	@RequestMapping(value = "visits/{reservedTimeId}/ticket/save/redis", method = RequestMethod.GET)
//	public void saveToRedis(@PathVariable Long reservedTimeId, HttpServletRequest request) throws IOException, TemplateException {
//		cacheManager.cache(reservedTimeRepository.findOne(reservedTimeId), request.getSession().getId());
//	}

//	@RequestMapping(value = "visits/ticket/get/redis.html", method = RequestMethod.GET)
//	public String getTicketFromRedis(HttpServletRequest request) throws IOException, TemplateException {
//		TicketModel model = new TicketModel(cacheManager.getValue(request.getSession().getId()));
//		return templateGenerator.generateHtml(model, "ticket.ftl");
//	}

//	@RequestMapping(value = "visits", method = RequestMethod.GET)
//	public List<ReservedTime> getAll(HttpServletRequest request) {
//		List<ReservedTime> allKeys = cacheManager.getAllCachedReservedTimesExcept(request.getSession().getId());
//		return allKeys;
//	}
}
