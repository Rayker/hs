package ru.ardecs.hs.common.requestmodels;

import javax.validation.constraints.NotNull;

public class DoctorsRequestModel {
	@NotNull
	private Long specialityId;
	@NotNull
	private Long hospitalId;

	public Long getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Long specialityId) {
		this.specialityId = specialityId;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
}
