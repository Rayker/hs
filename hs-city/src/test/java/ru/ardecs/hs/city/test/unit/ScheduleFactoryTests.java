package ru.ardecs.hs.city.test.unit;

import org.junit.Test;
import ru.ardecs.hs.city.bl.ScheduleFactory;
import ru.ardecs.hs.common.entities.JobInterval;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ScheduleFactoryTests {
	private ScheduleFactory scheduleFactory;

	private final int visitDuration = 30 * 60 * 1000;

	public ScheduleFactoryTests() {
		scheduleFactory = new ScheduleFactory(visitDuration, "HH:mm");
	}

	@Test
	public void generateNumbersInIntervalTest() {
		JobInterval jobInterval = new JobInterval();
		jobInterval.setStartTime(new Time(10 * visitDuration));
		jobInterval.setEndTime(new Time(20 * visitDuration));

		List<Integer> collect = scheduleFactory.generateNumbersInInterval(jobInterval)
				.mapToObj(i -> i)
				.collect(Collectors.toList());
		assertEquals(10, collect.size());
	}
}
