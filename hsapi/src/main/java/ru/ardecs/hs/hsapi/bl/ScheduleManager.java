package ru.ardecs.hs.hsapi.bl;

import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hscommon.entities.ReservedTime;
import ru.ardecs.hs.hscommon.models.VisitModel;

import java.util.Date;
import java.util.List;

/**
 * Created by Rayker on 7/1/16.
 */
public interface ScheduleManager {
	// TODO: 6/30/16 add doctorId dependency
	List<Date> getWorkDays(Long doctorId, int dayCount);

	List<VisitModel> getVisitsByNotSessionId(Long doctorId, Date date, String sessionId);

	void cache(CachedVisit cachedVisit, String sessionId);

	ReservedTime save(ReservedTime reservedTime, String sessionId);
}
