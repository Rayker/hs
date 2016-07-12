package ru.ardecs.hs.hsapi.test.unit;

import org.junit.Before;
import org.junit.Test;
import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hsapi.cache.MemoryCacheManager;
import ru.ardecs.hs.hsapi.test.TestData;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MemoryCacheManagerTests {
	private final TestData testData = new TestData();
	private final int expireTimeInMinutes = 30;
	private MemoryCacheManager memoryCacheManager;

	@Before
	public void initTestCase() {
		memoryCacheManager = new MemoryCacheManager(expireTimeInMinutes);
	}

	@Test
	public void cacheAndGetTest() {
		memoryCacheManager.cache(testData.firstSessionId, testData.firstVisit);
		memoryCacheManager.cache(testData.secondSessionId, testData.secondVisit);
		List<CachedVisit> cachedVisits = memoryCacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(testData.doctorId, testData.date, testData.thirdSessionId);

		assertEquals(2, cachedVisits.size());
		assertTrue(cachedVisits.contains(testData.firstVisit));
		assertTrue(cachedVisits.contains(testData.secondVisit));
	}

	@Test
	public void filterBySessionIdTest() {
		memoryCacheManager.cache(testData.firstSessionId, testData.firstVisit);
		memoryCacheManager.cache(testData.secondSessionId, testData.secondVisit);
		List<CachedVisit> cachedVisits = memoryCacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(testData.doctorId, testData.date, testData.secondSessionId);

		assertEquals(1, cachedVisits.size());
		assertEquals(testData.firstVisit, cachedVisits.get(0));
	}

	@Test
	public void recacheTest() {
		memoryCacheManager.cache(testData.firstSessionId, testData.firstVisit);
		memoryCacheManager.cache(testData.firstSessionId, testData.secondVisit);
		List<CachedVisit> cachedVisits = memoryCacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(testData.doctorId, testData.date, testData.secondSessionId);

		assertEquals(1, cachedVisits.size());
		assertEquals(testData.secondVisit, cachedVisits.get(0));
	}

	@Test
	public void deleteTest() {
		memoryCacheManager.cache(testData.firstSessionId, testData.firstVisit);
		memoryCacheManager.delete(testData.firstSessionId);
		List<CachedVisit> cachedVisits = memoryCacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(testData.doctorId, testData.date, testData.secondSessionId);

		assertEquals(0, cachedVisits.size());
	}


}
