package ru.ardecs.hs.hsdb.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.ardecs.hs.hsdb.entities.Doctor;

public interface DoctorRepository  extends CrudRepository<Doctor, Long> {

}
