package ru.ardecs.hs.hsclient.db.entities;

import ru.ardecs.hs.common.entities.Speciality;

import javax.persistence.*;

@Entity
public class SummarySpecialityStatistic {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	@JoinColumn(name = "speciality_id", unique = true)
	private Speciality speciality = new Speciality();

	private int visitorsNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getVisitorsNumber() {
		return visitorsNumber;
	}

	public void setVisitorsNumber(int visitorsNumber) {
		this.visitorsNumber = visitorsNumber;
	}

	public Speciality getSpeciality() {
		return speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}
}
