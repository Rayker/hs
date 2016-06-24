package ru.ardecs.hs.hsapi.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hsdb.entities.JobInterval;
import ru.ardecs.hs.hsdb.repositories.DoctorRepository;
import ru.ardecs.hs.hsdb.repositories.ReservedTimeRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ScheduleManager {
	private final static int visitInMinutes = 30;
	private final static int visitInMilliseconds = visitInMinutes * 60 * 1000;
	private final static DateFormat timeFormat = new SimpleDateFormat("HH:mm");

	@Autowired
	private ReservedTimeRepository reservedTimeRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	public List<VisitModel> getTimes(Long doctorId, Date date) {
		Set<String> keys = getReservedTimesKeys(doctorId, date);

		return doctorRepository.findOne(doctorId).getJobIntervals()
				.stream()
				.flatMap(jobInterval ->
						generateArithmeticProgressionForInterval(jobInterval)
								.mapToObj(numberInInterval ->
										new VisitModel(
												numberInInterval,
												jobInterval.getId(),
												getVisitTime(jobInterval, numberInInterval),
												date,
												keys.contains(getKey(jobInterval, numberInInterval)))))
				.collect(Collectors.toList());
	}

	private String getKey(JobInterval jobInterval, int numberInInterval) {
		return "" + numberInInterval + "_" + jobInterval.getId();
	}

	public static String getVisitTime(JobInterval jobInterval, int numberInInterval) {
		long temp = jobInterval.getStartTime().getTime() + numberInInterval * visitInMilliseconds;
		return timeFormat.format(new java.util.Date(temp));
	}

	private IntStream generateArithmeticProgressionForInterval(JobInterval jobInterval) {
		return IntStream
				.range(0, (int) (jobInterval.getEndTime().getTime() - jobInterval.getStartTime().getTime() - 1) / visitInMilliseconds + 1);
	}

	private Set<String> getReservedTimesKeys(Long doctorId, Date date) {
		return reservedTimeRepository.findByJobIntervalDoctorIdAndDate(doctorId, new java.sql.Date(date.getTime()))
				.stream()
				.map(t -> getKey(t.getJobInterval(), t.getNumberInInterval()))
				.collect(Collectors.toSet());
	}
}
