package ru.ardecs.hs.city.cache;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MemoryCacheManager implements CacheManager {
	private final Object locker = new Object();
	private final Map<String, CachedNode> nodesBySessionId = new HashMap<>();
	private final SortedMap<Date, CachedNode> nodesByExpiredTime = new TreeMap<>();
	private final Multimap<String, CachedNode> nodesByDateAndDoctorId = HashMultimap.create();
	private final int expireTimeInMinutes;

	public MemoryCacheManager(int expireTimeInMinutes) {
		this.expireTimeInMinutes = expireTimeInMinutes;
	}

	@Override
	public void cache(String sessionId, CachedVisit cachedVisit) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, expireTimeInMinutes);
		CachedNode cachedNode = new CachedNode(sessionId, cachedVisit, calendar.getTime());

		delete(sessionId);

		synchronized (locker) {
			nodesBySessionId.put(cachedNode.getSessionId(), cachedNode);
			nodesByExpiredTime.put(cachedNode.getExpiredTime(), cachedNode);
			nodesByDateAndDoctorId.put(cachedNode.getDateAndDoctorIdKey(), cachedNode);
		}
	}

	@Override
	public List<CachedVisit> getCachedVisitsByDoctorIdAndDateAndNotSessionId(Long doctorId, Date date, String sessionId) {
		synchronized (locker) {
			return nodesByDateAndDoctorId.get(CachedNode.createDateAndDoctorIdKey(date, doctorId))
					.stream()
					.filter(node -> !node.getSessionId().equals(sessionId))
					.map(CachedNode::getCachedVisit)
					.collect(Collectors.toList());
		}
	}

	@Override
	public void delete(String sessionId) {
		synchronized (locker) {
			CachedNode deletedNode = nodesBySessionId.get(sessionId);
			if (deletedNode == null) {
				return;
			}
			delete(deletedNode);
		}
	}

	private boolean tryDeleteByLeastExpiredTime() {
		synchronized (locker) {
			if (nodesByExpiredTime.isEmpty()) {
				return false;
			}
			Date expiredTime = nodesByExpiredTime.firstKey();
			if (new Date().compareTo(expiredTime) > 0) {
				delete(nodesByExpiredTime.get(expiredTime));
				return true;
			}
			return false;
		}
	}

	@Scheduled(fixedDelayString = "${application.cache.checkDelay}")
	private void deleteExpired() {
		while (tryDeleteByLeastExpiredTime()) {
		}
	}

	private void delete(CachedNode deletedNode) {
		nodesByExpiredTime.remove(deletedNode.getExpiredTime());
		nodesBySessionId.remove(deletedNode.getSessionId());
		nodesByDateAndDoctorId.remove(deletedNode.getDateAndDoctorIdKey(), deletedNode);
	}
}

class CachedNode {
	private static final SimpleDateFormat innerKeyDateFormat = new SimpleDateFormat("yyyyMMdd_");
	private final CachedVisit cachedVisit;
	private final Date expiredTime;
	private final String sessionId;
	private final String dateAndDoctorIdKey;

	CachedNode(String sessionId, CachedVisit cachedVisit, Date cacheTime) {
		this.cachedVisit = cachedVisit;
		this.expiredTime = cacheTime;
		this.sessionId = sessionId;
		this.dateAndDoctorIdKey = createDateAndDoctorIdKey(cachedVisit.getDate(), cachedVisit.getDoctorId());
	}

	static String createDateAndDoctorIdKey(Date date, Long doctorId) {
		return innerKeyDateFormat.format(date) + doctorId;
	}

	CachedVisit getCachedVisit() {
		return cachedVisit;
	}

	Date getExpiredTime() {
		return expiredTime;
	}

	String getSessionId() {
		return sessionId;
	}

	String getDateAndDoctorIdKey() {
		return dateAndDoctorIdKey;
	}
}