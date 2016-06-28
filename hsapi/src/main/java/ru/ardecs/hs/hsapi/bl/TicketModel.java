package ru.ardecs.hs.hsapi.bl;

import ru.ardecs.hs.hsdb.entities.ReservedTime;

import java.util.Date;

public class TicketModel {
	private String hospital;
	private String doctor;
	private String doctorSpeciality;
	private Date visitDate;
	private String visitTime;
	private long visitId;
	private String visitorName;
	private Date visitorBirthday;

	public TicketModel() {}

	public TicketModel(ReservedTime reservedTime) {
		this.hospital = reservedTime.getJobInterval().getDoctor().getHospital().getName();
		this.doctor = reservedTime.getJobInterval().getDoctor().getFullname();
		this.doctorSpeciality = reservedTime.getJobInterval().getDoctor().getSpeciality().getName();
		this.visitDate = reservedTime.getDate();
		this.visitTime = ScheduleManager.getVisitTime(reservedTime.getJobInterval(), reservedTime.getNumberInInterval());
		this.visitId = reservedTime.getId();
		this.visitorName = reservedTime.getVisitorName();
		this.visitorBirthday = reservedTime.getVisitorBirthday();
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

	public long getVisitId() {
		return visitId;
	}

	public void setVisitId(long visitId) {
		this.visitId = visitId;
	}

	public String getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public Date getVisitorBirthday() {
		return visitorBirthday;
	}

	public void setVisitorBirthday(Date visitorBirthday) {
		this.visitorBirthday = visitorBirthday;
	}
}
