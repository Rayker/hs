package ru.ardecs.hs.hsapi.bl;

import ru.ardecs.hs.hsdb.entities.ReservedTime;

import java.util.Date;

public class TicketModel {
	private String hospital;
	private String doctor;
	private String doctorSpeciality;
	private Date visitDate;
	private String visitTime;
	private long cancellationCode;

	public TicketModel() {}

	public TicketModel(ReservedTime reservedTime) {
		this.hospital = reservedTime.getJobInterval().getDoctor().getHospital().getName();
		this.doctor = reservedTime.getJobInterval().getDoctor().getFullname();
		this.doctorSpeciality = reservedTime.getJobInterval().getDoctor().getSpeciality().getName();
		this.visitDate = reservedTime.getDate();
		this.visitTime = ScheduleManager.getVisitTime(reservedTime.getJobInterval(), reservedTime.getNumberInInterval());
		this.cancellationCode = reservedTime.getId(); // FIXME: 6/24/16
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getDoctorSpeciality() {
		return doctorSpeciality;
	}

	public void setDoctorSpeciality(String doctorSpeciality) {
		this.doctorSpeciality = doctorSpeciality;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public long getCancellationCode() {
		return cancellationCode;
	}

	public void setCancellationCode(long cancellationCode) {
		this.cancellationCode = cancellationCode;
	}

	public String getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}
}
