package ru.ardecs.hs.hsdb;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ardecs.hs.hsdb.repositories.SpecialityRepository;

public class DbConnector {
	@Autowired
	private SpecialityRepository repository;
}
