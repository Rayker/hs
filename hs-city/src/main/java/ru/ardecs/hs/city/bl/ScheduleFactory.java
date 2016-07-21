package ru.ardecs.hs.city.bl;

import ru.ardecs.hs.common.entities.JobInterval;
import ru.ardecs.hs.common.entities.ReservedTime;
import ru.ardecs.hs.common.models.TicketModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.IntStream;

public class ScheduleFactory {
	// TODO: 7/8/16 make thread safe
	private final DateFormat timeFormat;
	private final long visitInMilliseconds;

	public ScheduleFactory(int visitInMilliseconds, String timeFormatPattern) {
		this.visitInMilliseconds = visitInMilliseconds;
		timeFormat = new SimpleDateFormat(timeFormatPattern);
	}

	public TicketModel createTicketModel(ReservedTime reservedTime) {
		TicketModel model = new TicketModel();
		model.setHospital(reservedTime.getJobInterval().getDoctor().getHospital().getName());
		model.setDoctor(reservedTime.getJobInterval().getDoctor().getFullname());
		model.setDoctorSpeciality(reservedTime.getJobInterval().getDoctor().getSpeciality().getName());
		model.setVisitDate(reservedTime.getDate());
		model.setVisitTime(getVisitTime(reservedTime.getJobInterval(), reservedTime.getNumberInInterval()));
		model.setVisitId(reservedTime.getId());
		model.setVisitorName(reservedTime.getVisitorName());
		model.setVisitorBirthday(reservedTime.getVisitorBirthday());
		return model;
	}

	public String getVisitTime(JobInterval jobInterval, int numberInInterval) {
		long visitTimeInMilliseconds = jobInterval.getStartTime().getTime() + numberInInterval * visitInMilliseconds;
		return timeFormat.format(new java.util.Date(visitTimeInMilliseconds));
	}

	public IntStream generateNumbersInInterval(JobInterval jobInterval) {
		long visitsInIntervalCount = ((jobInterval.getEndTime().getTime() - jobInterval.getStartTime().getTime() - 1) / visitInMilliseconds + 1);
		return IntStream.range(0, (int) visitsInIntervalCount);
	}

}
