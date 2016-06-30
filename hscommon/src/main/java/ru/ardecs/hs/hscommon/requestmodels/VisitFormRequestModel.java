package ru.ardecs.hs.hscommon.requestmodels;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class VisitFormRequestModel implements Serializable {
	private long jobIntervalId;
	private int numberInInterval;
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date date;

	public long getJobIntervalId() {
		return jobIntervalId;
	}

	public int getNumberInInterval() {
		return numberInInterval;
	}

	public Date getDate() {
		return date;
	}

	public void setJobIntervalId(long jobIntervalId) {
		this.jobIntervalId = jobIntervalId;
	}

	public void setNumberInInterval(int numberInInterval) {
		this.numberInInterval = numberInInterval;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
