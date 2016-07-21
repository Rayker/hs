package ru.ardecs.hs.city.bl;

import ru.ardecs.hs.city.cache.CachedVisit;
import ru.ardecs.hs.common.entities.ReservedTime;
import ru.ardecs.hs.common.models.VisitModel;

import java.util.Date;
import java.util.List;

public interface ScheduleManager {
	// TODO: 6/30/16 add doctorId dependency
	List<Date> getWorkDays(Long doctorId, int dayCount);

	List<VisitModel> getVisitsByNotSessionId(Long doctorId, Date date, String sessionId);

	void cache(CachedVisit cachedVisit, String sessionId);

	ReservedTime save(ReservedTime reservedTime, String sessionId);
}
