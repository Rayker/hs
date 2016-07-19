package ru.ardecs.hs.hsclient.availability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ardecs.hs.hsclient.api.ApiProvider;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class AvailabilityService {
	private static Logger logger = LoggerFactory.getLogger(AvailabilityService.class);

	@Autowired
	private ApiProvider apiProvider;

	private ArrayList<CompletableFuture> completableFutureList = new ArrayList<>();

	@Scheduled(cron = "${application.checkAvailability.cron}")
	private void checkAvailability() {
		logger.debug("checkAvailability(): start");
		apiProvider.getApiWrappers().forEach((cityId, wrapper) -> {
			CompletableFuture<Boolean> pingFuture = CompletableFuture.supplyAsync(wrapper::ping);
			pingFuture.thenAccept(ok -> {
				if (!ok) {
					stop();
					logger.error("City with cityId = {} is not available", cityId);
				} else {
					logger.debug("City with cityId = {} is available", cityId);
				}
			});

			completableFutureList.add(pingFuture);
		});
	}

	private synchronized void stop() {
		completableFutureList.forEach(future -> future.cancel(true));
		completableFutureList.clear();
	}
}
