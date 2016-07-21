package ru.ardecs.hs.common.models;

import java.util.Date;

public class VisitModel {
	private int numberInInterval;
	private long jobIntervalId;
	private String visitTime;
	private Date date;
	private boolean reserved;

	public VisitModel(int numberInInterval, long jobIntervalId, String visitTime, Date date, boolean reserved) {
		this.numberInInterval = numberInInterval;
		this.jobIntervalId = jobIntervalId;
		this.visitTime = visitTime;
		this.date = date;
		this.reserved = reserved;
	}

	public int getNumberInInterval() {
		return numberInInterval;
	}

	public void setNumberInInterval(int numberInInterval) {
		this.numberInInterval = numberInInterval;
	}

	public String getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public long getJobIntervalId() {
		return jobIntervalId;
	}

	public void setJobIntervalId(long jobIntervalId) {
		this.jobIntervalId = jobIntervalId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
