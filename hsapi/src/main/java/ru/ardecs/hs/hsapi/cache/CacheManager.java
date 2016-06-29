package ru.ardecs.hs.hsapi.cache;

import java.util.stream.Stream;

public interface CacheManager {
	void cache(CachedVisit cachedVisit, String sessionId);

	Stream<CachedVisit> getAllCachedReservedTimesExcept(String sessionId);

	void delete(String sessionId);
}
