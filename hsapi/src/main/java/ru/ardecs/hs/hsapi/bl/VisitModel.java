package ru.ardecs.hs.hsapi.bl;

public class VisitModel {
	private int numberInInterval;
	private long intervalId;
	private String visitTime;
	private boolean reserved;

	public VisitModel(int numberInInterval, long intervalId, String visitTime, boolean reserved) {
		this.numberInInterval = numberInInterval;
		this.intervalId = intervalId;
		this.visitTime = visitTime;
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

	public long getIntervalId() {
		return intervalId;
	}

	public void setIntervalId(long intervalId) {
		this.intervalId = intervalId;
	}
}
