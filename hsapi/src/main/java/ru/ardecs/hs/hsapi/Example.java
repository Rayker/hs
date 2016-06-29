package ru.ardecs.hs.hsapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsdb.entities.ReservedTime;

@Component
public class Example {
	public static final String RESERVED_TIME_NAME = "reservedTime";
	@Autowired
	private RedisTemplate<String, Object> template;

	public void save(ReservedTime reservedTime) {
		template.opsForValue().set(RESERVED_TIME_NAME, reservedTime);
	}

	public ReservedTime getValue(Long reservedTimeId) {
		return (ReservedTime) template.opsForValue().get(RESERVED_TIME_NAME);
	}

//	public void addLink(String userId, URL url) {
//		listOps.leftPush(userId, url.toExternalForm());
//		// or use template directly
//		redisTemplate.boundListOps(userId).leftPush(url.toExternalForm());
//	}
}
