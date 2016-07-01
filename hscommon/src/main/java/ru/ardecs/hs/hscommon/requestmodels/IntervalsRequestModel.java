package ru.ardecs.hs.hscommon.requestmodels;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class IntervalsRequestModel {
	@NotNull
	private Long doctorId;

	@NotNull
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
