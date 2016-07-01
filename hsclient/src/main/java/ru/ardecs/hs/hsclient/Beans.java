package ru.ardecs.hs.hsclient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsclient.api.ApiProvider;
import ru.ardecs.hs.hsclient.db.CityApiRepository;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.util.Properties;

@Component
public class Beans {
	@Bean
	public CloseableHttpClient httpClient() {
		return HttpClients.createDefault();
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

	@Bean(name = "app.props")
	public Properties getApplicationProperties() throws IOException {
		ClassPathResource resource = new ClassPathResource("application.properties");
		Properties properties = new Properties();
		properties.load(resource.getInputStream());
		return properties;
	}
//
//	@Bean
//	public ApiProvider getApiProvider(CityApiRepository repository) {
//		repository.findAll().spliterator();
//	}
}
