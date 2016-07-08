package ru.ardecs.hs.hsapi.bl;

import ru.ardecs.hs.hscommon.entities.JobInterval;
import ru.ardecs.hs.hscommon.entities.ReservedTime;
import ru.ardecs.hs.hscommon.models.TicketModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ScheduleFactory {
	// TODO: 7/8/16 add property timeFormat
	private final DateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private final int visitInMilliseconds;

	public ScheduleFactory(int visitInMilliseconds) {
		this.visitInMilliseconds = visitInMilliseconds;
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
}
