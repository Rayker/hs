package ru.ardecs.hs.hsapi.test.integration;

import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.ardecs.hs.hsapi.bl.ScheduleManager;
import ru.ardecs.hs.hsapi.cache.CacheManager;
import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hsapi.test.TestData;
import ru.ardecs.hs.hscommon.entities.ReservedTime;
import ru.ardecs.hs.hscommon.models.VisitModel;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, Mocks.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ScheduleManagerTests {
	private final TestData testData = new TestData();

	@Autowired
	private ScheduleManager scheduleManager;

	@Autowired
	private CacheManager cacheManager;

	@Test
	public void cacheTest() {
		scheduleManager.cache(testData.firstVisit, testData.firstSessionId);

		List<CachedVisit> cachedVisits = cacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(testData.doctorId, testData.date, testData.thirdSessionId);
		assertEquals(cachedVisits.size(), 1);
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
		assertEquals(cachedVisits.size(), 0);
	}
}
