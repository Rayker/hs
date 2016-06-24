package ru.ardecs.hs.hsapi.bl;

import ru.ardecs.hs.hsdb.entities.ReservedTime;

import java.sql.Date;

public class VisitModel {
	private int numberInInterval;
	private long intervalId;
	private String visitTime;
	private Date date;
	private boolean reserved;

	public VisitModel(int numberInInterval, long intervalId, String visitTime, Date date, boolean reserved) {
		this.numberInInterval = numberInInterval;
		this.intervalId = intervalId;
		this.visitTime = visitTime;
		this.date = date;
		this.reserved = reserved;
	}

	public VisitModel(ReservedTime reservedTime) {
		this.numberInInterval = reservedTime.getNumberInInterval();
		this.intervalId = reservedTime.getJobInterval().getId();
		this.visitTime = ScheduleManager.getVisitTime(reservedTime.getJobInterval(), reservedTime.getNumberInInterval());
		this.date = reservedTime.getDate();
		this.reserved = true;
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

	public long getIntervalId() {
		return intervalId;
	}

	public void setIntervalId(long intervalId) {
		this.intervalId = intervalId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
