package ru.ardecs.hs.hsapi.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsapi.cache.CacheManager;
import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hscommon.models.VisitModel;
import ru.ardecs.hs.hscommon.entities.JobInterval;
import ru.ardecs.hs.hscommon.entities.ReservedTime;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class ScheduleManagerImpl implements ScheduleManager {
	private final static int visitInMinutes = 30;
	private final static int visitInMilliseconds = visitInMinutes * 60 * 1000;
	private final static DateFormat timeFormat = new SimpleDateFormat("HH:mm");

	@Autowired
	@Qualifier("MemoryCacheManager")
	private CacheManager cacheManager;

	@Autowired
	private ReservedTimeRepository reservedTimeRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	// TODO: 6/29/16 move or refactor
	private String getVisitTime(JobInterval jobInterval, int numberInInterval) {
		long temp = jobInterval.getStartTime().getTime() + numberInInterval * visitInMilliseconds;
		return timeFormat.format(new java.util.Date(temp));
	}

	// TODO: 6/30/16 add doctorId dependency
	@Override
	public List<Date> getWorkDays(Long doctorId, int dayCount) {
		Calendar calendar = Calendar.getInstance();
		return IntStream.range(0, dayCount)
				.mapToObj(i -> {
					calendar.add(Calendar.DATE, 1);
					return calendar.getTime();
				})
				.collect(Collectors.toList());
	}

	@Override
	public List<VisitModel> getVisitsByNotSessionId(Long doctorId, Date date, String sessionId) {
		Set<String> reservedTimesKeys = getReservedTimesKeysByNotSessionId(doctorId, date, sessionId);

		return doctorRepository
				.findOne(doctorId)
				.getJobIntervals()
				.stream()
				.flatMap(jobInterval -> generateArithmeticProgressionForInterval(jobInterval)
						.mapToObj(
								numberInInterval -> new VisitModel(
										numberInInterval,
										jobInterval.getId(),
										getVisitTime(jobInterval, numberInInterval),
										date,
										reservedTimesKeys.contains(getKey(jobInterval.getId(), numberInInterval)))))
				.collect(Collectors.toList());
	}

	@Override
	public void cache(CachedVisit cachedVisit, String sessionId) {
		cacheManager.cache(cachedVisit, sessionId);
	}

	@Override
	public ReservedTime save(ReservedTime reservedTime, String sessionId) {
		cacheManager.delete(sessionId);
		return reservedTimeRepository.save(reservedTime);
	}

	private Set<String> getReservedTimesKeysByNotSessionId(Long doctorId, Date date, String sessionId) {
		return Stream
				.concat(getConfirmedVisits(doctorId, date),
						getCachedVisitsByNotSessionId(doctorId, date, sessionId))
				.collect(Collectors.toSet());
	}

	private Stream<String> getConfirmedVisits(Long doctorId, Date date) {
		return reservedTimeRepository
				.findByJobIntervalDoctorIdAndDate(doctorId, new java.sql.Date(date.getTime()))
				.stream()
				.map(reservedTime -> getKey(reservedTime.getJobInterval().getId(), reservedTime.getNumberInInterval()));
	}

	private Stream<String> getCachedVisitsByNotSessionId(Long doctorId, Date date, String sessionId) {
		return cacheManager.getCachedVisitsByDoctorIdAndDateAndNotSessionId(doctorId, date, sessionId)
				.stream()
				.map(v -> getKey(v.getJobIntervalId(), v.getNumberInInterval()));
	}

	private String getKey(Long jobIntervalId, int numberInInterval) {
		return "" + numberInInterval + "_" + jobIntervalId;
	}

	private IntStream generateArithmeticProgressionForInterval(JobInterval jobInterval) {
		return IntStream
				.range(0, (int) (jobInterval.getEndTime().getTime() - jobInterval.getStartTime().getTime() - 1) / visitInMilliseconds + 1);
	}
}
