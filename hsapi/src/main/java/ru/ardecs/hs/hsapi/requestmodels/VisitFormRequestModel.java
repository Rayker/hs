package ru.ardecs.hs.hsapi.requestmodels;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class VisitFormRequestModel {
	private long intervalId;
	private int numberInInterval;
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date date;

	public long getIntervalId() {
		return intervalId;
	}

	public int getNumberInInterval() {
		return numberInInterval;
	}

	public Date getDate() {
		return date;
	}

	public void setIntervalId(long intervalId) {
		this.intervalId = intervalId;
	}

	public void setNumberInInterval(int numberInInterval) {
		this.numberInInterval = numberInInterval;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
