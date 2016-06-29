package ru.ardecs.hs.hsdb.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String fullname;

	@ManyToOne
	@JoinColumn(name = "specialities_id")
	private Speciality speciality;

	@ManyToOne
	@JoinColumn(name = "hospitals_id")
	private Hospital hospital;

	@OneToMany(mappedBy = "doctor")
	@JsonIgnore
	private List<JobInterval> jobIntervals;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Speciality getSpeciality() {
		return speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public List<JobInterval> getJobIntervals() {
		return jobIntervals;
	}

	public void setJobIntervals(List<JobInterval> jobIntervals) {
		this.jobIntervals = jobIntervals;
	}
}
