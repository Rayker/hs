package ru.ardecs.hs.hsapi.test.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.ardecs.hs.hsapi.bl.ScheduleManager;
import ru.ardecs.hs.hsapi.cache.CacheManager;
import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hsapi.test.TestBeans;
import ru.ardecs.hs.hsapi.test.TestData;
import ru.ardecs.hs.hscommon.entities.ReservedTime;
import ru.ardecs.hs.hscommon.models.VisitModel;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, ScheduleManagerTests.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Configuration
public class ScheduleManagerTests {
	private final TestData testData = new TestData();
	@Autowired
	private ScheduleManager scheduleManager;
	@Autowired
	private CacheManager cacheManager;

	@Bean
	public DoctorRepository doctorRepository() {
		DoctorRepository mock = mock(DoctorRepository.class);
		when(mock.findOne(testData.doctorId)).thenReturn(testData.doctor);
		return mock;
	}

	@Bean
	public ReservedTimeRepository reservedTimeRepository() {
		ReservedTimeRepository mock = mock(ReservedTimeRepository.class);
		when(mock.findByJobIntervalDoctorIdAndDate(eq(testData.doctorId), any(Date.class)))
				.thenReturn(Collections.singletonList(testData.reservedTime));
		return mock;
	}

	@Test
	public void cacheTest() {
		scheduleManager.cache(testData.firstVisit, testData.firstSessionId);

		List<CachedVisit> cachedVisits = cacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(testData.doctorId, testData.date, testData.thirdSessionId);
		assertEquals(1, cachedVisits.size());
	}

	@Test
	public void getVisitsFromCacheAndRepositoryTest() {
		scheduleManager.cache(testData.firstVisit, testData.firstSessionId);
		scheduleManager.save(new ReservedTime(), testData.firstSessionId);
		scheduleManager.cache(testData.secondVisit, testData.secondSessionId);
		List<VisitModel> visits = scheduleManager.getVisitsByNotSessionId(testData.doctorId, testData.date, testData.thirdSessionId);

		assertTrue(visits.get(testData.firstNumberInInterval).isReserved());
		assertTrue(visits.get(testData.secondNumberInInterval).isReserved());
	}

	@Test
	public void cacheAndSaveTest() {
		scheduleManager.cache(testData.firstVisit, testData.firstSessionId);
		scheduleManager.save(new ReservedTime(), testData.firstSessionId);
		List<CachedVisit> cachedVisits = cacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(testData.doctorId, testData.date, testData.thirdSessionId);
		assertEquals(0, cachedVisits.size());
	}
}
