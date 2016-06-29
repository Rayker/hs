package ru.ardecs.hs.hsapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsdb.entities.ReservedTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Example {
	public static final String RESERVED_TIME_NAME = "reservedTime";

	@Autowired
	private RedisTemplate<String, Object> template;

	public void save(ReservedTime reservedTime, String sessionId) {
		template.opsForValue().set(getKey(sessionId), reservedTime);
	}

	private String getKey(String sessionId) {
		return RESERVED_TIME_NAME + ":" + sessionId;
	}

	public ReservedTime getValue(String sessionId) {
		return (ReservedTime) template.opsForValue().get(getKey(sessionId));
	}

	public List<ReservedTime> getAllKeys() {
		Set<byte[]> keys = template.getConnectionFactory().getConnection().keys(("*" + getKey("*")).getBytes());
		List<String> collect = keys.stream()
				.map(bs -> new String(bs))
				.map(s -> s.substring(7))
				.collect(Collectors.toList());
		List<ReservedTime> list = new ArrayList<>();
		for (String key : collect) {
			list.add((ReservedTime) template.opsForValue().get(key));
		}
		return list;
	}

//	public List<String> getAllKeys() {
//		Set<String> keys = template.keys("*");
//		return keys.stream().collect(Collectors.toList());
//	}

//	public void addLink(String userId, URL url) {
//		listOps.leftPush(userId, url.toExternalForm());
//		// or use template directly
//		redisTemplate.boundListOps(userId).leftPush(url.toExternalForm());
//	}
}
