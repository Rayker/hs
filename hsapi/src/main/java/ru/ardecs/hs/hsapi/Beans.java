package ru.ardecs.hs.hsapi;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsapi.cache.CachedVisit;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.util.Properties;

@Component
public class Beans {
	@Bean
	public RedisTemplate<String, CachedVisit> redisTemplate(RedisConnectionFactory rc) {
		final RedisTemplate<String, CachedVisit> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(rc);
		return redisTemplate;
	}
}
