package ru.ardecs.hs.hsapi;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.util.Properties;

@Component
public class Beans {
	private Configuration cfg;

	@Bean(name = "freemarker.configuration")
	public Configuration getTemplateConfiguration(@Value("${application.ftlBaseFolder}") String baseDir) {
		if (cfg == null) {
			cfg = new Configuration(Configuration.VERSION_2_3_23);
			cfg.setClassForTemplateLoading(getClass(), baseDir);
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
//		    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
			cfg.setAutoFlush(true);
		}
		return cfg;
	}

	@Bean(name = "app.props")
	public Properties getApplicationProperties() throws IOException {
		ClassPathResource resource = new ClassPathResource("application.properties");
		Properties properties = new Properties();
		properties.load(resource.getInputStream());
		return properties;
	}

	@Bean
	public Session getMailSession(@Qualifier("app.props") Properties properties,
	                              @Value("${mail.from.username}") String username,
	                              @Value("${mail.from.password}") String password) {
		return Session.getInstance(properties,
				new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory rc) {
		final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(rc);
		return redisTemplate;
	}
}
