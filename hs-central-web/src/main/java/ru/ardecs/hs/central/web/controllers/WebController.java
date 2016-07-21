package ru.ardecs.hs.central.web.controllers;

import freemarker.template.TemplateException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.ardecs.hs.central.api.ApiProvider;
import ru.ardecs.hs.central.mail.MailSender;
import ru.ardecs.hs.common.TemplateGenerator;
import ru.ardecs.hs.common.models.TicketModel;
import ru.ardecs.hs.common.models.VisitModel;
import ru.ardecs.hs.common.requestmodels.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;

// TODO: 6/30/16 add deleting and refactor
@Controller
@RequestMapping("/cities/{cityId}")
public class WebController {
	@Autowired
	private ApiProvider apiProvider;

	@Autowired
	private TemplateGenerator templateGenerator;

	@Autowired
	private CloseableHttpClient httpclient;

	@Autowired
	private MailSender mailSender;

	@RequestMapping(value = "/specialities.html", method = RequestMethod.GET)
	public String specialities(@PathVariable Long cityId, Model model) throws IOException, URISyntaxException {
		model.addAttribute("specialities", apiProvider.getApiWrapper(cityId).specialities());
		model.addAttribute("cityId", cityId);
		return "specialities";
	}

	@RequestMapping(value = "/hospitals.html", method = RequestMethod.GET)
	public String hospitals(@PathVariable Long cityId, @Validated HospitalsRequestModel requestModel, Model model) throws IOException, URISyntaxException {
		model.addAttribute("specialityId", requestModel.getSpecialityId());
		model.addAttribute("hospitals", apiProvider.getApiWrapper(cityId).hospitals(requestModel));
		model.addAttribute("cityId", cityId);
		return "hospitals";
	}

	@RequestMapping(value = "/doctors.html", method = RequestMethod.GET)
	public String doctors(@PathVariable Long cityId, @Validated DoctorsRequestModel doctorsRequestModel, Model model) throws IOException, URISyntaxException {
		model.addAttribute("specialityId", doctorsRequestModel.getSpecialityId());
		model.addAttribute("hospitalId", doctorsRequestModel.getHospitalId());
		model.addAttribute("doctors", apiProvider.getApiWrapper(cityId).doctors(doctorsRequestModel));
		model.addAttribute("cityId", cityId);
		return "doctors";
	}

	@RequestMapping(value = "/doctors/workdays.html", params = "doctorId")
	public String choseDate(@PathVariable Long cityId, Long doctorId, Model model) throws IOException, URISyntaxException {
		model.addAttribute("dates", apiProvider.getApiWrapper(cityId).choseDate(doctorId));
		model.addAttribute("doctorId", doctorId);
		model.addAttribute("cityId", cityId);
		return "dates";
	}

	@RequestMapping(value = "/visits/all.html", method = RequestMethod.GET)
	public String times(@PathVariable Long cityId, @Validated IntervalsRequestModel intervalsRequestModel, Model model, HttpSession session) throws IOException, URISyntaxException {
		model.addAttribute("date", intervalsRequestModel.getDate());
		model.addAttribute("visits", apiProvider.getApiWrapper(cityId).times(intervalsRequestModel, session.getId()));
		model.addAttribute("cityId", cityId);
		return "visitTimes";
	}

	// TODO: 7/7/16 replace to VisitFormModel
	@RequestMapping(value = "/visits/new.html", method = RequestMethod.POST)
	public String getVisitForm(@PathVariable Long cityId, @Validated VisitFormRequestModel visitFormRequestModel, Model model, HttpSession session) throws IOException, URISyntaxException {
//		apiWrapperImpl.sendPost("/cache/visits.json", visitFormRequestModel);

		apiProvider.getApiWrapper(cityId).cache(visitFormRequestModel, session.getId());

		// TODO: 7/7/16 replace to VisitFormModel
		VisitModel visitModel = new VisitModel(
				visitFormRequestModel.getNumberInInterval(),
				visitFormRequestModel.getJobIntervalId(),
				null,
				visitFormRequestModel.getDate(),
				false);
		model.addAttribute("model", visitModel);
		model.addAttribute("cityId", cityId);
		return "visitForm";
	}

	// TODO: 7/7/16 replace to VisitFormModel
	@RequestMapping(value = "/visits", method = RequestMethod.POST)
	public String createVisit(@PathVariable Long cityId, @Validated VisitCreatingRequestModel visitCreatingRequestModel, HttpSession session) throws IOException, URISyntaxException {
		long reservedTimeId = apiProvider.getApiWrapper(cityId).createVisit(visitCreatingRequestModel, session.getId());
		return "redirect:/cities/" + cityId + "/visits/" + reservedTimeId + "/ticket.html";
	}

	@RequestMapping(value = "/visits/{reservedTimeId}/ticket.html", method = RequestMethod.GET)
	public String getTicket(@PathVariable Long cityId, @PathVariable Long reservedTimeId, Model model) throws IOException, URISyntaxException {
		TicketModel ticketModel = apiProvider.getApiWrapper(cityId).getTicketModel(reservedTimeId);
		model.addAttribute("model", ticketModel);
		model.addAttribute("cityId", cityId);
		return "ticket";
	}

	// TODO: 6/30/16 move?
	@RequestMapping(value = "/visits/{reservedTimeId}/ticket/send", method = RequestMethod.POST, params = {"addressTo"})
	public String send(@PathVariable Long cityId, @PathVariable Long reservedTimeId, String addressTo) throws IOException, TemplateException {
		mailSender.send(cityId, addressTo, reservedTimeId);
		return "redirect:/visits/" + reservedTimeId + "/ticket.html";
	}
}
