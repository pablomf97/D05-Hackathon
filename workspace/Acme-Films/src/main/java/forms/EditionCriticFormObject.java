package forms;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import domain.Critic;
import domain.Curricula;

public class EditionCriticFormObject {

	/* Attributes */

	/* Actor attributes */
	private String username;
	private String password;

	private int id;
	private int version;
	private String name;
	private String surname;
	private String photo;
	private String email;
	private String phoneNumber;
	private String address;
	private Curricula curricula;

	public EditionCriticFormObject() {

	}

	public EditionCriticFormObject(Critic critic) {
		this.id = critic.getId();
		this.version = critic.getVersion();
		this.username = critic.getUserAccount().getUsername();
		this.password = critic.getUserAccount().getPassword();
		this.name = critic.getName();
		this.surname = critic.getSurname();
		this.photo = critic.getPhoto();
		this.email = critic.getEmail();
		this.phoneNumber = critic.getPhoneNumber();
		this.address = critic.getAddress();
		this.curricula = critic.getCurricula();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Size(min = 5, max = 32)
	@Column(unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Size(min = 5, max = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@URL
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@NotBlank
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Valid
	public Curricula getCurricula() {
		return curricula;
	}

	public void setCurricula(Curricula curricula) {
		this.curricula = curricula;
	}

}