package ru.ardecs.hs.city.cache;

import java.util.Date;
import java.util.List;

public interface CacheManager {
	void cache(String sessionId, CachedVisit cachedVisit);

	List<CachedVisit> getCachedVisitsByDoctorIdAndDateAndNotSessionId(Long doctorId, Date date, String sessionId);

	void delete(String sessionId);
}
