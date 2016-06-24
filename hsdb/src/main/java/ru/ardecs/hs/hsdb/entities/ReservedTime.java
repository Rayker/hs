package ru.ardecs.hs.hsdb.entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "reserved_times")
public class ReservedTime {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "job_intervals_id")
	private JobInterval jobInterval = new JobInterval();

	@Column(name = "number_in_interval")
	private int numberInInterval;

	private Date date;

	public ReservedTime() {}

	public ReservedTime(Long jobIntervalId, int numberInInterval, java.sql.Date date) {
		jobInterval.setId(jobIntervalId);
		this.numberInInterval = numberInInterval;
		this.date = date;
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
}
