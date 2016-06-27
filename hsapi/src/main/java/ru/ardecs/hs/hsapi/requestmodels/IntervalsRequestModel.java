package ru.ardecs.hs.hsapi.requestmodels;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class IntervalsRequestModel {
	private Long doctorId;
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date date;

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
