package ru.ardecs.hs.hsdb.repositories;

import javax.persistence.Entity;

@Entity
public class TestEntity {
	private int visitorsNumber;
	private int specialityId;

	public int getVisitorsNumber() {
		return visitorsNumber;
	}

	public void setVisitorsNumber(int visitorsNumber) {
		this.visitorsNumber = visitorsNumber;
	}

	public int getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(int specialityId) {
		this.specialityId = specialityId;
	}
}
