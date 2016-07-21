package ru.ardecs.hs.central.api;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.central.db.entities.CityApi;
import ru.ardecs.hs.central.db.repositories.CityApiRepository;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

@Component
public class ApiProvider {
	@Autowired
	private CityApiRepository cityApiRepository;

	@Autowired
	private CloseableHttpClient httpClient;

	private Map<Long, ApiWrapper> apiWrappers = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		StreamSupport
				.stream(cityApiRepository.findAll().spliterator(), false)
				.forEach(this::load);
	}

	private ApiWrapper load(CityApi e) {
		return getApiWrappers().put(
				e.getId(),
				new ApiWrapperImpl(httpClient, e.getHost(), e.getPort()));
	}

	public ApiWrapper getApiWrapper(Long cityId) {
		return getApiWrappers().get(cityId);
	}

	public Map<Long, ApiWrapper> getApiWrappers() {
		return apiWrappers;
	}
}
