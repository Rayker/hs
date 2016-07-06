package ru.ardecs.hs.hsclient.db.entities;

import ru.ardecs.hs.hscommon.entities.Speciality;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class CityStatistic {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Date date;

	private int visitsNumber;

	@ManyToOne
	@JoinColumn(name = "specialities_id")
	private Speciality speciality = new Speciality();

	@ManyToOne
	@JoinColumn(name = "city_id")
	private CityApi cityApi = new CityApi();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getVisitsNumber() {
		return visitsNumber;
	}

	public void setVisitsNumber(int visitsNumber) {
		this.visitsNumber = visitsNumber;
	}

	public Speciality getSpeciality() {
		return speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

	public CityApi getCityApi() {
		return cityApi;
	}

	public void setCityApi(CityApi cityApi) {
		this.cityApi = cityApi;
	}
}
