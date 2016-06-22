package ru.ardecs.hs.hsapi.hello;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;
import ru.ardecs.hs.hsdb.entities.Speciality;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;

import java.io.IOException;

@RestController
public class HelloController {
	@Autowired
	private SpecialityRepository repository;

	@RequestMapping(value = "/specialities", method = RequestMethod.GET)
	@ResponseBody
	public Iterable<Speciality> specialities(Pageable pageable) throws IOException {
		return repository.findAll(pageable);
	}
}