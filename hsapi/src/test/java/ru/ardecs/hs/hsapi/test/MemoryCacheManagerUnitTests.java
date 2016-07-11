package ru.ardecs.hs.hsapi.test;

import org.junit.Before;
import org.junit.Test;
import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hsapi.cache.MemoryCacheManager;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MemoryCacheManagerUnitTests {
	private final String firstSessionId = "firstSessionId";
	private final String secondSessionId = "secondSessionId";
	private final String thirdSessionId = "thirdSessionId";
	private final Date date = new Date();
	private final long doctorId = 1L;
	private final CachedVisit firstVisit = new CachedVisit(doctorId, 1L, 0, date);
	private final CachedVisit secondVisit = new CachedVisit(doctorId, 1L, 1, date);
	private final int expireTimeInMinutes = 30;
	private MemoryCacheManager memoryCacheManager;

	@Before
	public void initTestCase() {
		memoryCacheManager = new MemoryCacheManager(expireTimeInMinutes);
	}

	@Test
	public void cacheAndGetTest() {
		memoryCacheManager.cache(firstSessionId, firstVisit);
		memoryCacheManager.cache(secondSessionId, secondVisit);
		List<CachedVisit> cachedVisits = memoryCacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(doctorId, date, thirdSessionId);

		assertEquals(cachedVisits.size(), 2);
		assertTrue(cachedVisits.contains(firstVisit));
		assertTrue(cachedVisits.contains(secondVisit));
	}

	@Test
	public void filterBySessionIdTest() {
		memoryCacheManager.cache(firstSessionId, firstVisit);
		memoryCacheManager.cache(secondSessionId, secondVisit);
		List<CachedVisit> cachedVisits = memoryCacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(doctorId, date, secondSessionId);

		assertEquals(cachedVisits.size(), 1);
		assertEquals(cachedVisits.get(0), firstVisit);
	}

	@Test
	public void recacheTest() {
		memoryCacheManager.cache(firstSessionId, firstVisit);
		memoryCacheManager.cache(firstSessionId, secondVisit);
		List<CachedVisit> cachedVisits = memoryCacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(doctorId, date, secondSessionId);

		assertEquals(cachedVisits.size(), 1);
		assertEquals(cachedVisits.get(0), secondVisit);
	}

	@Test
	public void deleteTest() {
		memoryCacheManager.cache(firstSessionId, firstVisit);
		memoryCacheManager.delete(firstSessionId);
		List<CachedVisit> cachedVisits = memoryCacheManager
				.getCachedVisitsByDoctorIdAndDateAndNotSessionId(doctorId, date, secondSessionId);

		assertEquals(cachedVisits.size(), 0);
	}


}
