package ru.ardecs.hs.hsapi.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;

@RestController
@EnableJpaRepositories(basePackages = {"ru.ardecs.hs.hsdb.repositories"})
@EntityScan("ru.ardecs.hs.hsdb.*")
public class HelloController {
	@Autowired
	private SpecialityRepository repository;

	@RequestMapping("/")
	public String index() {
		return getRepository().findOne(1L).getName();
	}

	public SpecialityRepository getRepository() {
		return repository;
	}

	public void setRepository(SpecialityRepository repository) {
		this.repository = repository;
	}
}