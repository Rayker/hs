package ru.ardecs.hs.hsapi.cache;

import java.util.Date;
import java.util.List;

public interface CacheManager {
	void cache(CachedVisit cachedVisit, String sessionId);

	List<CachedVisit> getCachedVisitsByDoctorIdAndDateAndNotSessionId(Long doctorId, Date date, String sessionId);

	void delete(String sessionId);
}
