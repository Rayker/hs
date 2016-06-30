package ru.ardecs.hs.hscommon.requestmodels;

public class DoctorsRequestModel {
	private long specialityId;
	private long hospitalId;

	public long getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(long specialityId) {
		this.specialityId = specialityId;
	}

	public long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(long hospitalId) {
		this.hospitalId = hospitalId;
	}
}
