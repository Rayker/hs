package ru.ardecs.hs.hsapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsdb.entities.Doctor;
import ru.ardecs.hs.hsdb.entities.Hospital;
import ru.ardecs.hs.hsdb.entities.Speciality;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.HospitalRepository;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;
import freemarker.template.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@RestController
public class WebController {
	private Configuration cfg;

	@PostConstruct
	public void init() throws IOException {
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setClassForTemplateLoading(getClass(), "/ftl/");
		cfg.setDefaultEncoding("UTF-8");
//		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		cfg.setAutoFlush(true);
	}

	@Autowired
	private SpecialityRepository specialityRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private HospitalRepository hospitalRepository;

	@RequestMapping(value = "/specialities.html", method = RequestMethod.GET)
	public String specialities(Pageable pageable) throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<>();
		map.put("specialities", specialityRepository.findAll(pageable).getContent());
		Template template = cfg.getTemplate("specialities.ftl");
		try (Writer output = new StringWriter()) {
			template.process(map, output);
			return output.toString();
		}
	}

	@RequestMapping(value = "/hospitals.html", method = RequestMethod.POST, params = {"specialityId"})
	@ResponseBody
	public List<Hospital> hospitals(Long specialityId, Pageable pageable) {
		return doctorRepository.queryHospitalsBySpecialityId(specialityId, pageable);
	}

	@RequestMapping(value = "/doctors.html", method = RequestMethod.POST, params = {"specialityId", "hospitalId"})
	@ResponseBody
	public List<Doctor> doctors(Long specialityId, Long hospitalId, Pageable pageable) {
		return doctorRepository.findBySpecialityIdAndHospitalId(specialityId, hospitalId, pageable);
	}
}
