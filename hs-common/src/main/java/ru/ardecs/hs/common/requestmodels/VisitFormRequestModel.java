package ru.ardecs.hs.common.requestmodels;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class VisitFormRequestModel implements Serializable {
	@NotNull
	private Long jobIntervalId;

	@NotNull
	private Integer numberInInterval;

	@NotNull
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date date;

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
}
