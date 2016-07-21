package ru.ardecs.hs.hsapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.common.entities.Speciality;
import ru.ardecs.hs.hsdb.services.DoctorService;

// TODO: 7/7/16 remove
@RestController
public class TempController {
	private static Logger logger = LoggerFactory.getLogger(TempController.class);

	@Autowired
	private DoctorService doctorService;

	@RequestMapping(value = "/doctor", method = RequestMethod.POST)
	public void temp(@RequestParam Long doctorId, @RequestParam Long specialityId, @RequestParam Long hospitalId) {
		doctorService.createDoctor(doctorId, specialityId, hospitalId);
	}

	@RequestMapping(value = "/doctorByName", method = RequestMethod.POST)
	public void temp(@RequestParam String doctorName, @RequestParam boolean rollback) {
		try {
			doctorService.createDoctor(doctorName, rollback);
//			doctorService.createHospital(doctorName, rollback);
		} catch (RuntimeException e) {
			logger.error("Error", e);
		}
	}

	@RequestMapping(value = "/specialities", method = RequestMethod.POST)
	public Speciality createSpeciality(@RequestParam String specialityName, @RequestParam long delay) throws InterruptedException {
		return doctorService.createSpeciality(specialityName, delay);
	}


	@RequestMapping(value = "/specialities", method = RequestMethod.GET)
	public Iterable<Speciality> specialities(@RequestParam(required = false) boolean now) {
		if (now) {
			return doctorService.getAllSpecialitiesNow();
		} else {
			return doctorService.getAllSpecialities();
		}
	}

}
