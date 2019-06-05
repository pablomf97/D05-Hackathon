
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

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Forum extends DomainEntity {

	//Attributes

	private String						name;
	private String						description;
	private Date						creationDate;
	private String						rejectReason;
	private boolean						isActive;
	private FilmEnthusiast				creator;
	private Collection<FilmEnthusiast>	groupMembers;
	private Saga						sagaAbout;
	private Film						filmAbout;
	private Moderator					moderator;


	//Getters and Setters

	@NotBlank
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@Type(type="text")
	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getCreationDate() {
		return this.creationDate;
	}
	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getRejectReason() {
		return this.rejectReason;
	}
	public void setRejectReason(final String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public boolean getIsActive() {
		return this.isActive;
	}
	public void setIsActive(final boolean isActive) {
		this.isActive = isActive;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public FilmEnthusiast getCreator() {
		return this.creator;
	}
	public void setCreator(final FilmEnthusiast creator) {
		this.creator = creator;
	}

	@Valid
	@NotNull
	@ManyToMany
	public Collection<FilmEnthusiast> getGroupMembers() {
		return this.groupMembers;
	}
	public void setGroupMembers(final Collection<FilmEnthusiast> groupMembers) {
		this.groupMembers = groupMembers;
	}

	@Valid
	@ManyToOne(optional = true)
	public Saga getSagaAbout() {
		return this.sagaAbout;
	}
	public void setSagaAbout(final Saga sagaAbout) {
		this.sagaAbout = sagaAbout;
	}

	@Valid
	@ManyToOne(optional = true)
	public Film getFilmAbout() {
		return this.filmAbout;
	}
	public void setFilmAbout(final Film filmAbout) {
		this.filmAbout = filmAbout;
	}

	@Valid
	@ManyToOne(optional = true)
	public Moderator getModerator() {
		return this.moderator;
	}
	public void setModerator(final Moderator moderator) {
		this.moderator = moderator;
	}

}
