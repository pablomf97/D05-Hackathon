package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity{
	
	//Attributes
	private String keyWord;
	private Double maximumDuration;
	private Double minimumRating;
	private Double maximumRating;
	private Date searchMoment;
	private Collection<Film> results;
	
	//Getters and setters
	@Length(max = 100)
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	@Min(value = 0L, message = "The value must be positive")
	@Max(value = 10000L, message = "The value must be positive")
	public Double getMaximumDuration() {
		return maximumDuration;
	}
	public void setMaximumDuration(Double maximumDuration) {
		this.maximumDuration = maximumDuration;
	}
	@Min(value = 0L, message = "The value must be positive")
	@Max(value = 10L, message = "The value must be positive")
	public Double getMinimumRating() {
		return minimumRating;
	}
	public void setMinimumRating(Double minimumRating) {
		this.minimumRating = minimumRating;
	}
	@Min(value = 0L, message = "The value must be positive")
	@Max(value = 0L, message = "The value must be positive")
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
	
	@Valid
	@ManyToMany
	public Collection<Film> getResults() {
		return results;
	}
	public void setResults(Collection<Film> results) {
		this.results = results;
	}
	
	
	
}
