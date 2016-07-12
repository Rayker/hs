package ru.ardecs.hs.hsapi.test.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ardecs.hs.hsapi.test.TestData;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class Mocks {
	private TestData testData = new TestData();

	@Bean
	public DoctorRepository doctorRepository() {
		DoctorRepository mock = mock(DoctorRepository.class);
		when(mock.findOne(testData.doctorId)).thenReturn(testData.doctor);
		return mock;
	}

	@Bean
	public ReservedTimeRepository reservedTimeRepository() {
		ReservedTimeRepository mock = mock(ReservedTimeRepository.class);
		when(mock.findByJobIntervalDoctorIdAndDate(eq(testData.doctorId), any()))
				.thenReturn(Arrays.asList(testData.reservedTime));
		return mock;
	}
}
