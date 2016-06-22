package ru.ardecs.hs.hsdb.entities;

import javax.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {
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
}
