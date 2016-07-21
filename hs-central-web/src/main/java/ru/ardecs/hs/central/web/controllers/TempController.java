package ru.ardecs.hs.central.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

// TODO: 7/5/16 remove
@Controller
public class TempController {

	@RequestMapping("/helloWorld")
	public String helloWorld(Model model) {
		model.addAttribute("message", "Hello World!");
		return "helloWorld";
	}
}
