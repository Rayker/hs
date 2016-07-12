package ru.ardecs.hs.hsapi.test.integration.statistic;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import ru.ardecs.hs.hsapi.HsapiApplication;
import ru.ardecs.hs.hsapi.test.integration.TestBeans;
import ru.ardecs.hs.hscommon.entities.Doctor;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestBeans.class})
@ContextConfiguration(classes = {PersistenceContext.class})
//@ContextConfiguration(locations = "classpath:persistence.xml")
//@ContextConfiguration(locations = "classpath:dbUnitContext.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("classpath:statistic.xml")
public class TempDbUnitTests {
	@Autowired
	private DoctorRepository doctorRepository;

	@Test
	public void testTest() {
		assertNotNull(doctorRepository);
		Doctor one = doctorRepository.findOne((Long) 2L);
	}
}