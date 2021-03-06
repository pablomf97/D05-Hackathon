package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Past;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity{
	
	//Attributes
	private String keyWord;
	private Double maximumDuration;
	private Double minimumRating;
	private Double maximumRating;
	private Date searchMoment;
	
	//Getters and setters
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public Double getMaximumDuration() {
		return maximumDuration;
	}
	public void setMaximumDuration(Double maximumDuration) {
		this.maximumDuration = maximumDuration;
	}
	public Double getMinimumRating() {
		return minimumRating;
	}
	public void setMinimumRating(Double minimumRating) {
		this.minimumRating = minimumRating;
	}
	public Double getMaximumRating() {
		return maximumRating;
	}
	public void setMaximumRating(Double maximumRating) {
		this.maximumRating = maximumRating;
	}
	
	@Past
	public Date getSearchMoment() {
		return searchMoment;
	}
	public void setSearchMoment(Date searchMoment) {
		this.searchMoment = searchMoment;
	}
	
	
	
}
