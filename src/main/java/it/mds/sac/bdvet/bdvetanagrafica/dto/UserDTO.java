package it.mds.sac.bdvet.bdvetanagrafica.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import it.mds.sac.comsvc.abs.dto.GenericDTO;
import it.mds.sac.sac.comsvc.encryption.annotation.Encrypt;

@Component
public class UserDTO extends GenericDTO {

	private String name;

	private String surname;

	//@Email
	private String email;

	@Encrypt
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
		return "UserDTO [name=" + name + ", surname=" + surname + ", email=" + email + ", password=" + password
				+ ", " + super.toString() + "]";
	}

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return null;
	}


}
