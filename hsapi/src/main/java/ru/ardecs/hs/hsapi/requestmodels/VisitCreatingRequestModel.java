package ru.ardecs.hs.hsapi.requestmodels;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class VisitCreatingRequestModel {
	private long jobIntervalId;
	private int numberInInterval;
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date date;
	private String visitorName;
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date visitorBirthday;

	public long getJobIntervalId() {
		return jobIntervalId;
	}

	public void setJobIntervalId(long jobIntervalId) {
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
