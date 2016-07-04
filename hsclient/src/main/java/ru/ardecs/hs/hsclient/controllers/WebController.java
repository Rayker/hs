package ru.ardecs.hs.hsclient.controllers;

import freemarker.template.TemplateException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsclient.api.ApiProvider;
import ru.ardecs.hs.hsclient.db.CityApiRepository;
import ru.ardecs.hs.hsclient.mail.MailSender;
import ru.ardecs.hs.hscommon.TemplateGenerator;
import ru.ardecs.hs.hscommon.models.TicketModel;
import ru.ardecs.hs.hscommon.models.VisitModel;
import ru.ardecs.hs.hscommon.requestmodels.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

// TODO: 6/30/16 add deleting
@RestController
public class WebController {
	@Autowired
	private CityApiRepository cityApiRepository;

	@Autowired
	private ApiProvider apiProvider;

	@Autowired
	private TemplateGenerator templateGenerator;

	@Autowired
	private CloseableHttpClient httpclient;

	@Autowired
	private MailSender mailSender;

	@RequestMapping(value = "/cities.html", method = RequestMethod.GET)
	public String cities() throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<>();
		map.put("cities", cityApiRepository.findAll());
		return templateGenerator.generateHtml(map, "cities.ftl");
	}

	@RequestMapping(value = "/cities/{cityId}/specialities.html", method = RequestMethod.GET)
	public String specialities(@PathVariable Long cityId) throws IOException, TemplateException, ClassNotFoundException, JSONException, URISyntaxException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialities", apiProvider.getApiWrapper(cityId).specialities());
		map.put("cityId", cityId);
		return templateGenerator.generateHtml(map, "specialities.ftl");
	}

	@RequestMapping(value = "/cities/{cityId}/hospitals.html", method = RequestMethod.GET)
	public String hospitals(@PathVariable Long cityId, @Validated HospitalsRequestModel requestModel) throws IOException, TemplateException, URISyntaxException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialityId", requestModel.getSpecialityId());
		map.put("hospitals", apiProvider.getApiWrapper(cityId).hospitals(requestModel));
		map.put("cityId", cityId);
		return templateGenerator.generateHtml(map, "hospitals.ftl");
	}

	@RequestMapping(value = "/cities/{cityId}/doctors.html", method = RequestMethod.GET)
	public String doctors(@PathVariable Long cityId, @Validated DoctorsRequestModel doctorsRequestModel) throws IOException, TemplateException, URISyntaxException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialityId", doctorsRequestModel.getSpecialityId());
		map.put("hospitalId", doctorsRequestModel.getHospitalId());
		map.put("doctors", apiProvider.getApiWrapper(cityId).doctors(doctorsRequestModel));
		map.put("cityId", cityId);
		return templateGenerator.generateHtml(map, "doctors.ftl");
	}

	@RequestMapping(value = "/cities/{cityId}/doctors/workdays.html", params = "doctorId")
	public String choseDate(@PathVariable Long cityId, Long doctorId) throws IOException, TemplateException, URISyntaxException {
		Map<String, Object> map = new HashMap<>();
		map.put("dates", apiProvider.getApiWrapper(cityId).choseDate(doctorId));
		map.put("doctorId", doctorId);
		map.put("cityId", cityId);
		return templateGenerator.generateHtml(map, "dates.ftl");
	}

	@RequestMapping(value = "/cities/{cityId}/visits/all.html", method = RequestMethod.GET)
	public String times(@PathVariable Long cityId, @Validated IntervalsRequestModel intervalsRequestModel, HttpSession session) throws IOException, TemplateException, URISyntaxException {
		Map<String, Object> map = new HashMap<>();
		map.put("date", intervalsRequestModel.getDate());
		map.put("visits", apiProvider.getApiWrapper(cityId).times(intervalsRequestModel, session.getId()));
		map.put("cityId", cityId);
		return templateGenerator.generateHtml(map, "visitTimes.ftl");
	}

	@RequestMapping(value = "/cities/{cityId}/visits/new.html", method = RequestMethod.POST)
	public String getVisitForm(@PathVariable Long cityId, @Validated VisitFormRequestModel visitFormRequestModel, HttpSession session) throws IOException, TemplateException, URISyntaxException {
//		apiWrapperImpl.sendPost("/cache/visits.json", visitFormRequestModel);

		apiProvider.getApiWrapper(cityId).cache(visitFormRequestModel, session.getId());

		VisitModel visitModel = new VisitModel(
				visitFormRequestModel.getNumberInInterval(),
				visitFormRequestModel.getJobIntervalId(),
				null,
				visitFormRequestModel.getDate(),
				false);
		Map<String, Object> map = new HashMap<>();
		map.put("model", visitModel);
		map.put("cityId", cityId);
		return templateGenerator.generateHtml(map, "visitForm.ftl");
	}

	@RequestMapping(value = "/cities/{cityId}/visits", method = RequestMethod.POST)
	public void createVisit(@PathVariable Long cityId, @Validated VisitCreatingRequestModel visitCreatingRequestModel, HttpServletResponse response, HttpSession session) throws IOException, URISyntaxException {
		long reservedTimeId = apiProvider.getApiWrapper(cityId).createVisit(visitCreatingRequestModel, session.getId());
		response.sendRedirect("/cities/" + cityId + "/visits/" + reservedTimeId + "/ticket.html");
	}

	@RequestMapping(value = "/cities/{cityId}/visits/{reservedTimeId}/ticket.html", method = RequestMethod.GET)
	public String getTicket(@PathVariable Long cityId, @PathVariable Long reservedTimeId) throws IOException, TemplateException, URISyntaxException {
		TicketModel model = apiProvider.getApiWrapper(cityId).getTicketModel(reservedTimeId);
		Map<String, Object> map = new HashMap<>();
		map.put("model", model);
		map.put("cityId", cityId);
		return templateGenerator.generateHtml(map, "ticket.ftl");
	}

	// TODO: 6/30/16 move?
	@RequestMapping(value = "/cities/{cityId}/visits/{reservedTimeId}/ticket/send", method = RequestMethod.POST, params = {"addressTo"})
	public void send(@PathVariable Long cityId, @PathVariable Long reservedTimeId, String addressTo, HttpServletResponse response) throws IOException, TemplateException {
		mailSender.send(cityId, addressTo, reservedTimeId);
		response.sendRedirect("/visits/" + reservedTimeId + "/ticket.html");
	}
}
