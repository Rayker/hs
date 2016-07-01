package ru.ardecs.hs.hsapi.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsapi.bl.ScheduleManagerImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RedisCacheManager implements CacheManager {
	private static final Logger logger =
			LoggerFactory.getLogger(ScheduleManagerImpl.class);

	@Value("${application.cache.expireInMinutes}")
	private long timeoutInMinutes;

	@Value("${application.redis.keyPrefix}")
	private String cachedVisitKeyPrefix;

	@Autowired
	private RedisTemplate<String, CachedVisit> template;

	@Override
	public void cache(CachedVisit cachedVisit, String sessionId) {
		template.opsForValue().set(getKey(sessionId), cachedVisit, timeoutInMinutes, TimeUnit.MINUTES);
	}

	private Stream<CachedVisit> getAllCachedVisitsExcept(String sessionId) {
		Set<byte[]> keys = template.getConnectionFactory().getConnection().keys(("*" + getKey("*")).getBytes());

		return keys.stream()
				.map(this::convertFromBytes)
				.filter(key -> !key.equals(getKey(sessionId)))
				.map(key -> template.opsForValue().get(key));
	}

	@Override
	public List<CachedVisit> getCachedVisitsByDoctorIdAndDateAndNotSessionId(Long doctorId, Date date, String sessionId) {
		return getAllCachedVisitsExcept(sessionId)
				.filter(v -> dateEquals(v.getDate(), date))
				.filter(v -> Objects.equals(v.getDoctorId(), doctorId))
				.collect(Collectors.toList());
	}

	@Override
	public void delete(String sessionId) {
		template.opsForValue().getOperations().delete(getKey(sessionId));
	}

	private String getKey(String sessionId) {
		return cachedVisitKeyPrefix + ":" + sessionId;
	}

	private boolean dateEquals(Date date1, Date date2) {
		// TODO: 6/29/16 refactor
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return fmt.format(date1).equals(fmt.format(date2));
	}

	private String convertFromBytes(byte[] bytes) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		     ObjectInput in = new ObjectInputStream(bis)) {
			return (String) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
