package ru.ardecs.hs.hsapi.controllers;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsapi.bl.TicketModel;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WebController {
	private Configuration cfg;

	@PostConstruct
	public void init() throws IOException {
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setClassForTemplateLoading(getClass(), "/ftl/");
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
//		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		cfg.setAutoFlush(true);
	}

	@Autowired
	private ReservedTimeRepository reservedTimeRepository;

	@Autowired
	private SpecialityRepository specialityRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@RequestMapping(value = "/specialities.html", method = RequestMethod.GET)
	public String specialities(Pageable pageable) throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialities", specialityRepository.findAll(pageable).getContent());
		return generateHtml(map, "specialities.ftl");
	}

	@RequestMapping(value = "/hospitals.html", method = RequestMethod.POST, params = {"specialityId"})
	public String hospitals(Long specialityId, Pageable pageable)   throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialityId", specialityId);
		map.put("hospitals", doctorRepository.queryHospitalsBySpecialityId(specialityId, pageable));
		return generateHtml(map, "hospitals.ftl");
	}

	@RequestMapping(value = "/doctors.html", method = RequestMethod.POST, params = {"specialityId", "hospitalId"})
	public String doctors(Long specialityId, Long hospitalId, Pageable pageable) throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialityId", specialityId);
		map.put("hospitalId", hospitalId);
		map.put("doctors", doctorRepository.findBySpecialityIdAndHospitalId(specialityId, hospitalId, pageable));
		return generateHtml(map, "doctors.ftl");
	}



	@RequestMapping(value = "visits/{reservedTimeId}/ticket.html", method = RequestMethod.GET)
	public String getTicket(@PathVariable Long reservedTimeId) throws IOException, TemplateException {
		TicketModel model = new TicketModel(reservedTimeRepository.findOne(reservedTimeId));
		Map<String, Object> map = new HashMap<>();
		map.put("model", model);
		return generateHtml(map, "ticket.ftl");
	}

	private String generateHtml(Map<String, Object> model, String name) throws IOException, TemplateException {
		Template template = cfg.getTemplate(name);
		try (Writer output = new StringWriter()) {
			template.process(model, output);
			return output.toString();
		}
	}
}
