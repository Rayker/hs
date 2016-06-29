package ru.ardecs.hs.hsapi.cache;

import java.io.Serializable;
import java.util.Date;

public class CachedVisit implements Serializable {
	private Long doctorId;
	private Long jobIntervalId;
	private int numberInInterval;
	private Date date;

	public CachedVisit(Long doctorId, Long jobIntervalId, int numberInInterval, Date date) {
		this.doctorId = doctorId;
		this.jobIntervalId = jobIntervalId;
		this.numberInInterval = numberInInterval;
		this.date = date;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Long getJobIntervalId() {
		return jobIntervalId;
	}

	public void setJobIntervalId(Long jobIntervalId) {
		this.jobIntervalId = jobIntervalId;
	}

	public int getNumberInInterval() {
		return numberInInterval;
	}

	public void setNumberInInterval(int numberInInterval) {
		this.numberInInterval = numberInInterval;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
