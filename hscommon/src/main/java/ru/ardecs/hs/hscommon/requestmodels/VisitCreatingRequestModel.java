package ru.ardecs.hs.hscommon.requestmodels;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class VisitCreatingRequestModel {
	@NotNull
	private Long jobIntervalId;

	@NotNull
	private Integer numberInInterval;

	@NotNull
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date date;

	@NotNull
	private String visitorName;

	@NotNull
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date visitorBirthday;

	public Long getJobIntervalId() {
		return jobIntervalId;
	}

	public void setJobIntervalId(Long jobIntervalId) {
		this.jobIntervalId = jobIntervalId;
	}

	public Integer getNumberInInterval() {
		return numberInInterval;
	}

	public void setNumberInInterval(Integer numberInInterval) {
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
