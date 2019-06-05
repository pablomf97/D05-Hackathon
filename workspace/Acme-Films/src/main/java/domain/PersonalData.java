
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class PersonalData extends DomainEntity {

	//Attributes

	private String	linkedIn;
	private String	fullName;
	private String	statement;
	private String	phoneNumber;


	//Getters and setters
	@URL
	@Pattern(regexp = "http(s)?:\\/\\/([\\w]+\\.)?linkedin\\.com\\/in\\/[A-z0-9_-]+\\/?")
	public String getLinkedIn() {
		return this.linkedIn;
	}
	public void setLinkedIn(final String linkedIn) {
		this.linkedIn = linkedIn;
	}

	@NotBlank
	public String getFullName() {
		return this.fullName;
	}
	public void setFullName(final String fullName) {
		this.fullName = fullName;
	}

	@NotBlank
	@Type(type="text")
	public String getStatement() {
		return this.statement;
	}
	public void setStatement(final String statement) {
		this.statement = statement;
	}
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
