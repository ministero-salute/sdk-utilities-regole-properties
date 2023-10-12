package it.mds.sac.bdvet.bdvetanagrafica.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import it.mds.sac.comsvc.abs.entity.GenericEntity;

@Entity
//@Table(name = "User")
public class UserEntity extends GenericEntity {

	@Id
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "SURNAME")
	private String surname;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PASSWORD")
	private String password;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				'}';
	}

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
