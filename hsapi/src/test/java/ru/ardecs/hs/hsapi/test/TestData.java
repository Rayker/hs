package ru.ardecs.hs.hsapi.test;

import ru.ardecs.hs.hsapi.cache.CachedVisit;
import ru.ardecs.hs.hscommon.entities.Doctor;
import ru.ardecs.hs.hscommon.entities.JobInterval;
import ru.ardecs.hs.hscommon.entities.ReservedTime;

import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class TestData {
	public final String firstSessionId = "firstSessionId";
	public final String secondSessionId = "secondSessionId";
	public final String thirdSessionId = "thirdSessionId";
	public final Date date = new Date();
	public final long doctorId = 1L;
	public final long jobIntervalId = 1L;
	public final int firstNumberInInterval = 0;
	public final int secondNumberInInterval = 1;

	public final CachedVisit firstVisit = new CachedVisit(doctorId, jobIntervalId, firstNumberInInterval, date);
	public final CachedVisit secondVisit = new CachedVisit(doctorId, jobIntervalId, secondNumberInInterval, date);
	public final Doctor doctor;
	public final JobInterval jobInterval;
	public final ReservedTime reservedTime;
	public final Time time_8_00;
	public final Time time_10_00;

	public TestData() {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		instance.set(Calendar.MILLISECOND, 0);

		instance.set(Calendar.HOUR_OF_DAY, 8);
		time_8_00 = new Time(instance.getTimeInMillis());

		instance.set(Calendar.HOUR_OF_DAY, 10);
		time_10_00 = new Time(instance.getTimeInMillis());

		jobInterval = new JobInterval();
		jobInterval.setId(jobIntervalId);
		jobInterval.setStartTime(time_8_00);
		jobInterval.setEndTime(time_10_00);

		this.doctor = new Doctor();
		doctor.setId(doctorId);
		doctor.setJobIntervals(Arrays.asList(jobInterval));

		reservedTime = new ReservedTime();
		reservedTime.setId(1L);
		reservedTime.setDate(new java.sql.Date(date.getTime()));
		reservedTime.setJobInterval(jobInterval);
		reservedTime.setNumberInInterval(firstNumberInInterval);
	}
}
