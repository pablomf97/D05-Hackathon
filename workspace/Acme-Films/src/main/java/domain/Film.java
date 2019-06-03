package domain;

import java.util.Collection;
import java.util.Date;


import javax.persistence.Access;
import javax.persistence.AccessType;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Film extends DomainEntity{

	//Attributes
	
	private String title;
	private String synopsis;
	private String poster;
	private Date releaseDate;
	private Integer runTime;
	private Double rating;

	private String ticker;
	private boolean isDraft;
	
	//Relationships
	private Moderator moderator;
	private Collection<Genre> genres;
	private Collection<Person> persons;
	private Collection<Saga>sagas;
	
	//Getters and setters
	
	@NotBlank
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotBlank
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	
	@URL
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	@Range(min=0)
	@NotNull
	public Integer getRunTime() {
		return runTime;
	}
	public void setRunTime(Integer runTime) {
		this.runTime = runTime;
	}
	

	@NotBlank
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public boolean getIsDraft() {
		return isDraft;
	}
	public void setIsDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Moderator getModerator() {
		return moderator;
	}
	public void setModerator(Moderator moderator) {
		this.moderator = moderator;
	}

	@Valid
	@ManyToMany
	public Collection<Genre> getGenres() {
		return genres;
	}
	public void setGenres(Collection<Genre> genres) {
		this.genres = genres;
	}
	
	@Valid
	@ManyToMany
	public Collection<Person> getPersons() {
		return persons;
	}
	public void setPersons(Collection<Person> persons) {
		this.persons = persons;
	}
	
	@Valid
	@ManyToMany
	public Collection<Saga> getSagas() {
		return sagas;
	}
	public void setSagas(Collection<Saga> sagas) {
		this.sagas = sagas;
	}
	
	@Range(min=0,max=10)
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	
	
	
	
}
