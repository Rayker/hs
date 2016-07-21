package ru.ardecs.hs.hsclient.controllers;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.ardecs.hs.hsclient.db.repositories.CityApiRepository;
import ru.ardecs.hs.hscommon.TemplateGenerator;

import java.io.IOException;

@Controller
public class CityController {
	@Autowired
	private CityApiRepository cityApiRepository;

	@Autowired
	private TemplateGenerator templateGenerator;

	@RequestMapping(value = {"/cities.html", "/"}, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView cities(ModelAndView modelAndView) throws IOException, TemplateException {
		modelAndView.addObject("cities", cityApiRepository.findAll());
		modelAndView.setViewName("cities");
		return modelAndView;
	}
}
