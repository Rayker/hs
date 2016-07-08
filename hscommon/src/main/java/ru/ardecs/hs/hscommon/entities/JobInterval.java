package ru.ardecs.hs.hscommon.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "job_intervals")
public class JobInterval implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "start_time")
	private Time startTime;

	@Column(name = "end_time")
	private Time endTime;

	@ManyToOne
	@JoinColumn(name = "doctors_id")
	private Doctor doctor;

	@OneToMany(mappedBy = "jobInterval")
	@JsonIgnore
	private List<ReservedTime> reservedTimes;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public List<ReservedTime> getReservedTimes() {
		return reservedTimes;
	}

	public void setReservedTimes(List<ReservedTime> reservedTimes) {
		this.reservedTimes = reservedTimes;
	}
}
