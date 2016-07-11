package ru.ardecs.hs.hsapi.test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;

import static org.mockito.Mockito.mock;

@Configuration
public class Mocks {


//	@InjectMocks
//	private DoctorRepository doctorRepository;
//
//	@Mock
//	private ReservedTimeRepository reservedTimeRepository;

	@Bean
	public DoctorRepository doctorRepository() {
		return mock(DoctorRepository.class);
	}

	@Bean
	public ReservedTimeRepository reservedTimeRepository() {
		return mock(ReservedTimeRepository.class);
	}
}
