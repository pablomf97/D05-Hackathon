package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsorship extends DomainEntity{

	//Attributes
	
	private String title;
	private String banner;
	private String targetPage;
	private boolean isActive;
	private Sponsor sponsor;
	private CreditCard creditCard;
	
	//Getters and setters
	
	@NotBlank
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@URL
	@NotBlank
	public String getBanner() {
		return banner;
	}
	public void setBanner(String banner) {
		this.banner = banner;
	}
	
	@URL
	@NotBlank
	public String getTargetPage() {
		return targetPage;
	}
	public void setTargetPage(String targetPage) {
		this.targetPage = targetPage;
	}
	
	@Valid 
	@NotNull
	@ManyToOne(optional = false)
	public Sponsor getSponsor() {
		return sponsor;
	}
	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}
	
	@Valid
	@NotNull
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
