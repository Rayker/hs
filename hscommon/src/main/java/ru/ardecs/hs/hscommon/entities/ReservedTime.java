package ru.ardecs.hs.hscommon.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "reserved_times")
public class ReservedTime implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "job_intervals_id")
	private JobInterval jobInterval = new JobInterval();

	private int numberInInterval;

	private Date date;

	private String visitorName;

	private Date visitorBirthday;

	public ReservedTime() {
	}

	public ReservedTime(Long jobIntervalId, int numberInInterval, java.util.Date date, String visitorName, java.util.Date visitorBirthday) {
		this.visitorName = visitorName;
		this.visitorBirthday = new Date(visitorBirthday.getTime());
		jobInterval.setId(jobIntervalId);
		this.numberInInterval = numberInInterval;
		this.date = new Date(date.getTime());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public JobInterval getJobInterval() {
		return jobInterval;
	}

	public void setJobInterval(JobInterval jobInterval) {
		this.jobInterval = jobInterval;
	}

	public int getNumberInInterval() {
		return numberInInterval;
	}

	public void setNumberInInterval(int numberInInterval) {
		this.numberInInterval = numberInInterval;
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
