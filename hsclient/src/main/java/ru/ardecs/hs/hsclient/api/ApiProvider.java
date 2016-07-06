package ru.ardecs.hs.hsclient.api;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsclient.db.entities.CityApi;
import ru.ardecs.hs.hsclient.db.repositories.CityApiRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

@Component
public class ApiProvider {
	@Autowired
	private CityApiRepository cityApiRepository;

	@Autowired
	private CloseableHttpClient httpClient;

	private Map<Long, ApiWrapper> apiWrappers = new HashMap<>();

	@PostConstruct
	private void init() {
		StreamSupport
				.stream(cityApiRepository.findAll().spliterator(),false)
				.forEach(this::load);
	}

	private ApiWrapper load(CityApi e) {
		return apiWrappers.put(
				e.getId(),
				new ApiWrapperImpl(
						httpClient,
						e.getHost(),
						e.getPort()));
	}

	public ApiWrapper getApiWrapper(Long cityId) {
		return apiWrappers.get(cityId);
	}
}
