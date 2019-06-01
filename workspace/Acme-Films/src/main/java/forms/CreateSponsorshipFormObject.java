package forms;

import java.util.Collection;

import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import domain.Film;

public class CreateSponsorshipFormObject {

	/* Attributes */

	/* Sponsorship attributes */
	private String title;
	private String banner;
	private String targetPage;
	private Collection<Film> films;

	/* Credit Card attributes */
	private String holder;
	private String make;
	private String number;
	private Integer expirationMonth;
	private Integer expirationYear;
	private Integer CVV;


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
	@ManyToMany
	public Collection<Film> getFilms() {
		return films;
	}
	public void setFilms(Collection<Film> films) {
		this.films = films;
	}

	@NotBlank
	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	@NotBlank
	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	@NotBlank
	@CreditCardNumber
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@NotNull
	@Range(min = 1, max = 12)
	public Integer getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@NotNull
	@Range(min = 0, max = 99)
	public Integer getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

	@NotNull
	@Range(min = 0, max = 999)
	public Integer getCVV() {
		return CVV;
	}

	public void setCVV(Integer CVV) {
		this.CVV = CVV;
	}

}