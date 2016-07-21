package ru.ardecs.hs.common.requestmodels;

import javax.validation.constraints.NotNull;

public class HospitalsRequestModel {
	@NotNull
	private Long specialityId;

	public Long getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Long specialityId) {
		this.specialityId = specialityId;
	}
}
