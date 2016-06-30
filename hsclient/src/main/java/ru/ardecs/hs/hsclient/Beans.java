package ru.ardecs.hs.hsclient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Beans {
	@Bean
	public CloseableHttpClient httpClient() {
		return HttpClients.createDefault();
	}
}
