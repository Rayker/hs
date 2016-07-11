package ru.ardecs.hs.hsapi.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.ardecs.hs.hsapi.bl.ScheduleManager;
import ru.ardecs.hs.hsapi.cache.CacheManager;
import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hscommon.entities.ReservedTime;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, Mocks.class})
public class ScheduleManagerTests {
	private final TestData testData = new TestData();

	@Autowired
	private ScheduleManager scheduleManager;

	@Autowired
	private CacheManager cacheManager;

	@Test
	public void cacheAndSaveTest() {
		scheduleManager.cache(testData.firstVisit, testData.firstSessionId);

		List<CachedVisit> cachedVisits = cacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(testData.doctorId, testData.date, testData.thirdSessionId);
		assertEquals(cachedVisits.size(), 1);

		scheduleManager.save(new ReservedTime(), testData.firstSessionId);
		cachedVisits = cacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(testData.doctorId, testData.date, testData.thirdSessionId);
		assertEquals(cachedVisits.size(), 0);
	}
}
