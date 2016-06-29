package ru.ardecs.hs.hsapi.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class CacheManager {
	@Value("${application.redis.keyPrefix}")
	private String cachedVisitKeyPrefix;

	@Value("${application.cache.timeoutInMinutes}")
	private long timeout;

	@Autowired
	private RedisTemplate<String, CachedVisit> template;

	public void cache(CachedVisit cachedVisit, String sessionId) {
		template.opsForValue().set(getKey(sessionId), cachedVisit, timeout, TimeUnit.MINUTES);
	}

	private String getKey(String sessionId) {
		return cachedVisitKeyPrefix + ":" + sessionId;
	}

	public List<CachedVisit> getAllCachedReservedTimesExcept(String sessionId) {
		Set<byte[]> keys = template.getConnectionFactory().getConnection().keys(("*" + getKey("*")).getBytes());
		return keys.stream()
				.map(String::new)
				.map(s -> s.substring(7))
				.filter(key -> !key.equals(getKey(sessionId)))
				.map(key -> template.opsForValue().get(key))
				.collect(Collectors.toList());
	}

	public void delete(String sessionId) {
		template.opsForValue().getOperations().delete(getKey(sessionId));
	}
}
