package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class MiscellaneousData extends DomainEntity{

	// Attributes

	private String text;
	private String attachements;

	// Getters and setters

	@NotBlank
	@Length(max=510)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@URL
	@Length(max=510)
	public String getAttachements() {
		return attachements;
	}

	public void setAttachements(String attachements) {
		this.attachements = attachements;
	}
}
