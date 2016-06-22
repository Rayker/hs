package ru.ardecs.hs.hsapi.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsdb.entities.Doctor;
import ru.ardecs.hs.hsdb.entities.Speciality;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;

import java.io.IOException;

@RestController
public class GeneralController {
	@Autowired
	private SpecialityRepository specialityRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@RequestMapping(value = "/specialities", method = RequestMethod.GET)
	@ResponseBody
	public Iterable<Speciality> specialities(Pageable pageable) throws IOException {
		return specialityRepository.findAll(pageable);
	}

	@RequestMapping(value = "/doctors", method = RequestMethod.GET)
	@ResponseBody
	public Iterable<Doctor> doctors() throws IOException {
		return doctorRepository.findAll();
	}
}