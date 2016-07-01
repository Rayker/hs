package ru.ardecs.hs.hscommon.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "hospitals")
public class Hospital implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}