package ru.ardecs.hs.hsapi.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
public class MemoryCacheManager implements CacheManager {
	private final Object locker = new Object();

	@Value("${application.cache.expireInMinutes}")
	private int timeoutInMinutes;

	private Map<String, CachedNode> nodesBySessionId = new HashMap<>();

	private SortedMap<Date, CachedNode> nodesByExpireTime = new TreeMap<>();

	@Override
	public void cache(CachedVisit cachedVisit, String sessionId) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, timeoutInMinutes);
		CachedNode cachedNode = new CachedNode(sessionId, cachedVisit, calendar.getTime());

		delete(sessionId);

		synchronized (locker) {
			nodesBySessionId.put(cachedNode.getSessionId(), cachedNode);
			nodesByExpireTime.put(cachedNode.getTimeOfExpire(), cachedNode);
		}
	}

	@Override
	public Stream<CachedVisit> getAllCachedReservedTimesExcept(String sessionId) {
		synchronized (locker) {
			return nodesBySessionId.values().stream()
					.filter(node -> !node.getSessionId().equals(sessionId))
					.map(CachedNode::getCachedVisit);
		}
	}

	@Override
	public void delete(String sessionId) {
		synchronized (locker) {
			CachedNode deletedNode = nodesBySessionId.get(sessionId);
			if (deletedNode == null) {
				return;
			}
			nodesByExpireTime.remove(deletedNode.getTimeOfExpire());
			nodesBySessionId.remove(deletedNode.getSessionId());
		}
	}

	private boolean tryDeleteByExpireTime() {
		// TODO: 6/29/16 refactor this
		synchronized (locker) {
			if (nodesByExpireTime.isEmpty()) {
				return false;
			}
			Date firstKey = nodesByExpireTime.firstKey();
			if (new Date().compareTo(firstKey) > 0) {
				CachedNode deletedNode = nodesByExpireTime.remove(firstKey);
				nodesBySessionId.remove(deletedNode.getSessionId());
				return true;
			} else {
				return false;
			}
		}
	}

	@Scheduled(fixedDelayString = "${application.cache.checkDelay}")
	private void deleteHead() {
		while (tryDeleteByExpireTime()) {
		}
	}
}

class CachedNode {
	private final CachedVisit cachedVisit;
	private final Date timeOfExpire;
	private final String sessionId;

	public CachedNode(String sessionId, CachedVisit cachedVisit, Date cacheTime) {
		this.cachedVisit = cachedVisit;
		this.timeOfExpire = cacheTime;
		this.sessionId = sessionId;
	}

	public CachedVisit getCachedVisit() {
		return cachedVisit;
	}

	public Date getTimeOfExpire() {
		return timeOfExpire;
	}

	public String getSessionId() {
		return sessionId;
	}
}