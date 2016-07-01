package ru.ardecs.hs.hsclient.controllers;

import freemarker.template.TemplateException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsclient.ApiWrapper;
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
	private TemplateGenerator templateGenerator;

	@Autowired
	private CloseableHttpClient httpclient;

	@Autowired
	private ApiWrapper apiWrapper;

	@RequestMapping(value = "/specialities.html", method = RequestMethod.GET)
	public String specialities() throws IOException, TemplateException, ClassNotFoundException, JSONException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialities", apiWrapper.specialities());
		return templateGenerator.generateHtml(map, "specialities.ftl");
	}

	@RequestMapping(value = "/hospitals.html", method = RequestMethod.GET, params = {"specialityId"})
	public String hospitals(HospitalsRequestModel requestModel) throws IOException, TemplateException, URISyntaxException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialityId", requestModel.getSpecialityId());
		map.put("hospitals", apiWrapper.hospitals(requestModel));
		return templateGenerator.generateHtml(map, "hospitals.ftl");
	}

	@RequestMapping(value = "/doctors.html", method = RequestMethod.GET, params = {"specialityId", "hospitalId"})
	public String doctors(DoctorsRequestModel doctorsRequestModel) throws IOException, TemplateException, URISyntaxException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialityId", doctorsRequestModel.getSpecialityId());
		map.put("hospitalId", doctorsRequestModel.getHospitalId());
		map.put("doctors", apiWrapper.doctors(doctorsRequestModel));
		return templateGenerator.generateHtml(map, "doctors.ftl");
	}

	@RequestMapping(value = "/doctors/workdays.html", params = "doctorId")
	public String choseDate(Long doctorId) throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<>();
		map.put("dates", apiWrapper.choseDate(doctorId));
		map.put("doctorId", doctorId);
		return templateGenerator.generateHtml(map, "dates.ftl");
	}

	@RequestMapping(value = "/visits/all.html", method = RequestMethod.GET, params = {"doctorId", "date"})
	public String times(IntervalsRequestModel intervalsRequestModel, HttpSession session) throws IOException, TemplateException, URISyntaxException {
		Map<String, Object> model = new HashMap<>();
		model.put("date", intervalsRequestModel.getDate());
//		new URIBuilder("http://localhost:8090/visits/all.json")
//				.setParameter("doctorId", String.valueOf(intervalsRequestModel.getDoctorId()))
//				.setParameter("date", String.valueOf(intervalsRequestModel.getDate()))
//				.setParameter("sessionId", session.getId());

		model.put("visits", apiWrapper.times(intervalsRequestModel, session.getId()));
		return templateGenerator.generateHtml(model, "visitTimes.ftl");
	}

//	private <T> T getValue(URI uri) throws IOException {
//		HttpGet httpGet = new HttpGet(uri);
//		CloseableHttpResponse response = httpclient.execute(httpGet);
//		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//		Type listType = new TypeToken<T>() {
//		}.getType();
//		return new Gson().fromJson(rd, listType);
//	}

	@RequestMapping(value = "/visits/new.html", method = RequestMethod.POST, params = {"date", "numberInInterval", "jobIntervalId"})
	public String getVisitForm(VisitFormRequestModel visitFormRequestModel, HttpSession session) throws IOException, TemplateException, URISyntaxException {
//		apiWrapperImpl.sendPost("/cache/visits.json", visitFormRequestModel);

		apiWrapper.cache(visitFormRequestModel, session.getId());

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
//		ReservedTime reservedTime = new ReservedTime(
//				visitCreatingRequestModel.getJobIntervalId(),
//				visitCreatingRequestModel.getNumberInInterval(),
//				visitCreatingRequestModel.getDate(),
//				visitCreatingRequestModel.getVisitorName(),
//				visitCreatingRequestModel.getVisitorBirthday());
//
//		ReservedTime savedVisit = scheduleManager.save(reservedTime, session.getId());

		long reservedTimeId = apiWrapper.createVisit(visitCreatingRequestModel, session.getId());
		response.sendRedirect("visits/" + reservedTimeId + "/ticket.html");
	}

	@RequestMapping(value = "/visits/{reservedTimeId}/ticket.html", method = RequestMethod.GET)
	public String getTicket(@PathVariable Long reservedTimeId) throws IOException, TemplateException {
//		TicketModel model = new TicketModel(reservedTimeRepository.findOne(reservedTimeId));
		TicketModel model = new TicketModel(apiWrapper.getReservedTime(reservedTimeId));
		return templateGenerator.generateHtml(model, "ticket.ftl");
	}
//
//	// TODO: 6/30/16 move?
//	@RequestMapping(value = "/visits/{reservedTimeId}/ticket/send", method = RequestMethod.POST, params = {"addressTo"})
//	public void send(@PathVariable Long reservedTimeId, String addressTo, HttpServletResponse response) throws IOException, TemplateException {
//		mailSender.send(addressTo, reservedTimeId);
//		response.sendRedirect("/visits/" + reservedTimeId + "/ticket.html");
//	}
}
