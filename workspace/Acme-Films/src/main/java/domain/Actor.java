package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import security.UserAccount;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Actor extends DomainEntity {

	// Atributes

	private String name;
	private String surname;
	private String photo;
	private String email;
	private String phoneNumber;
	private String address;
	private UserAccount userAccount;
	private boolean isSpammer;
	private Collection<SocialProfile> socialProfiles;
	private Collection<MessageBox> messageBoxes;

	// Getters and Setters

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

	@Length(max = 255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@NotNull
	@Valid
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public boolean getIsSpammer() {
		return isSpammer;
	}

	public void setIsSpammer(boolean isSpammer) {
		this.isSpammer = isSpammer;
	}

	@Valid
	@OneToMany
	public Collection<SocialProfile> getSocialProfile() {
		return socialProfiles;
	}

	public void setSocialProfile(Collection<SocialProfile> socialProfiles) {
		this.socialProfiles = socialProfiles;
	}

	@Valid
	@OneToMany
	public Collection<MessageBox> getMessageBoxes() {
		return messageBoxes;
	}

	public void setMessageBoxes(Collection<MessageBox> messageBoxes) {
		this.messageBoxes = messageBoxes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (isSpammer ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((photo == null) ? 0 : photo.hashCode());
		result = prime * result
				+ ((socialProfiles == null) ? 0 : socialProfiles.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result
				+ ((userAccount == null) ? 0 : userAccount.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actor other = (Actor) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (isSpammer != other.isSpammer)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (photo == null) {
			if (other.photo != null)
				return false;
		} else if (!photo.equals(other.photo))
			return false;
		if (socialProfiles == null) {
			if (other.socialProfiles != null)
				return false;
		} else if (!socialProfiles.equals(other.socialProfiles))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		if (userAccount == null) {
			if (other.userAccount != null)
				return false;
		} else if (!userAccount.equals(other.userAccount))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Actor [name=" + name + ", surname=" + surname + ", photo="
				+ photo + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", address=" + address + ", userAccount=" + userAccount
				+ ", isSpammer=" + isSpammer + ", socialProfile="
				+ socialProfiles + "]";
	}

}
