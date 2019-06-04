package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Comment extends DomainEntity {

	// Attributes
	private Date publishedMoment;
	private String body;
	private Double rating;
	private Film film;
	private Forum forum;
	private FilmEnthusiast filmEnthusiast;

	// Getters and setters

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getPublishedMoment() {
		return publishedMoment;
	}

	public void setPublishedMoment(Date publishedMoment) {
		this.publishedMoment = publishedMoment;
	}

	@NotBlank
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Range(min = 0, max = 10)
	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	@Valid
	@OneToOne(optional = true)
	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	@Valid
	@ManyToOne(optional = true)
	public Forum getForum() {
		return forum;
	}

	public void setForum(Forum forum) {
		this.forum = forum;
	}


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public FilmEnthusiast getFilmEnthusiast() {
		return filmEnthusiast;
	}


	public void setFilmEnthusiast(FilmEnthusiast filmEnthusiast) {
		this.filmEnthusiast = filmEnthusiast;
	}


}

