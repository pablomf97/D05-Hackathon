package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.Actor;
import domain.CreditCard;
import domain.Film;
import domain.Sponsor;
import domain.Sponsorship;
import forms.CreateSponsorshipFormObject;
import forms.EditSponsorshipFormObject;

@Transactional
@Service
public class SponsorshipService {
	
	// Managed repository ------------------------------------
	
	@Autowired
	private SponsorshipRepository sponsorshipRepository;
	
	// Supporting services -----------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private Validator validator;
	
	public Sponsorship create() {
		Actor principal;
		Sponsorship result;
		Collection<Film> films = new ArrayList<>();

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "SPONSOR"),
				"not.allowed");

		result = new Sponsorship();
		result.setFilms(films);
		result.setSponsor((Sponsor) principal);
		result.setIsActive(null);
		
		return result;
	}

	public Collection<Sponsorship> findAll() {
		Collection<Sponsorship> result;
		result = this.sponsorshipRepository.findAll();

		return result;
	}

	public Sponsorship findOne(final int sponsorshipId) {
		Sponsorship result;
		result = this.sponsorshipRepository.findOne(sponsorshipId);

		return result;
	}
	
	public Sponsorship save(final Sponsorship sponsorship) {
		Actor principal;
		Sponsorship result;

		principal = this.actorService.findByPrincipal();
		if(sponsorship.getId() != 0) {
			if(this.actorService.checkAuthority(principal, "MODERATOR")) {
				Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR"),
						"not.allowed");
			} else if (this.actorService.checkAuthority(principal, "MODERATOR")) {
				Assert.isTrue(sponsorship.getSponsor().equals((Sponsor) (principal)), "not.allowed");
			}
		}
		
		Assert.notNull(sponsorship.getTitle());
		Assert.notNull(sponsorship.getBanner());
		Assert.notNull(sponsorship.getTargetPage());
		Assert.notNull(sponsorship.getCreditCard());
		Assert.notNull(sponsorship.getSponsor());
		
		result = this.sponsorshipRepository.save(sponsorship);

		return result;
	}
	
	public void delete(final Sponsorship sponsorship) {
		Actor principal;

		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0, "wrong.id");

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(sponsorship.getSponsor().equals((Sponsor) principal),
				"not.allowed");

		this.sponsorshipRepository.delete(sponsorship.getId());
	}
	
	// Other business methods -------------------------------
	
	public Sponsorship reconstruct(CreateSponsorshipFormObject form,
			BindingResult binding) {

		/* Creating sponsorship */
		Sponsorship sponsorship = this.create();

		sponsorship.setTitle(form.getTitle());
		sponsorship.setBanner(form.getBanner());
		sponsorship.setTargetPage(form.getTargetPage());
		sponsorship.setFilms(form.getFilms());

		/* Creating credit card */
		CreditCard creditCard = new CreditCard();

		creditCard.setHolder(form.getHolder());
		creditCard.setMake(form.getMake());
		creditCard.setNumber(form.getNumber());
		creditCard.setExpirationMonth(form.getExpirationMonth());
		creditCard.setExpirationYear(form.getExpirationYear());
		creditCard.setCVV(form.getCVV());

		sponsorship.setCreditCard(creditCard);
		
		this.validator.validate(sponsorship, binding);

		/* Credit card */
		if (form.getNumber() != null) {
			try {
				Assert.isTrue(this.creditCardService
						.checkCreditCardNumber(creditCard.getNumber()),
						"card.number.error");
			} catch (Throwable oops) {
				binding.rejectValue("number", "number.error");
			}
		}

		if (creditCard.getExpirationMonth() != null
				&& creditCard.getExpirationYear() != null) {

			try {
				Assert.isTrue(
						!this.creditCardService.checkIfExpired(
								creditCard.getExpirationMonth(),
								creditCard.getExpirationYear()),
						"card.date.error");
			} catch (Throwable oops) {
				binding.rejectValue("expirationMonth", "card.date.error");
			}

			if (form.getCVV() != null) {
				try {
					Assert.isTrue(form.getCVV() < 999 && form.getCVV() > 100,
							"CVV.error");
				} catch (Throwable oops) {
					binding.rejectValue("CVV", "CVV.error");
				}
			}
		}
		return sponsorship;
	}
	
	public Sponsorship reconstruct(EditSponsorshipFormObject form,
			BindingResult binding) {

		/* Creating sponsorship */
		Sponsorship sponsorship = this.findOne(form.getId());

//		sponsorship.setId(form.getId());
//		sponsorship.setVersion(form.getVersion());
		
		if(sponsorship.getIsActive() == null) {
			
			sponsorship.setTitle(form.getTitle());
			sponsorship.setBanner(form.getBanner());
			sponsorship.setTargetPage(form.getTargetPage());
		}
		sponsorship.setFilms(form.getFilms());

		/* Creating credit card */
		CreditCard creditCard = new CreditCard();

		creditCard.setHolder(form.getHolder());
		creditCard.setMake(form.getMake());
		creditCard.setNumber(form.getNumber());
		creditCard.setExpirationMonth(form.getExpirationMonth());
		creditCard.setExpirationYear(form.getExpirationYear());
		creditCard.setCVV(form.getCVV());

		sponsorship.setCreditCard(creditCard);
		
		this.validator.validate(sponsorship, binding);

		/* Credit card */
		if (form.getNumber() != null) {
			try {
				Assert.isTrue(this.creditCardService
						.checkCreditCardNumber(creditCard.getNumber()),
						"card.number.error");
			} catch (Throwable oops) {
				binding.rejectValue("number", "number.error");
			}
		}

		if (creditCard.getExpirationMonth() != null
				&& creditCard.getExpirationYear() != null) {

			try {
				Assert.isTrue(
						!this.creditCardService.checkIfExpired(
								creditCard.getExpirationMonth(),
								creditCard.getExpirationYear()),
						"card.date.error");
			} catch (Throwable oops) {
				binding.rejectValue("expirationMonth", "card.date.error");
			}

			if (form.getCVV() != null) {
				try {
					Assert.isTrue(form.getCVV() < 999 && form.getCVV() > 100,
							"CVV.error");
				} catch (Throwable oops) {
					binding.rejectValue("CVV", "CVV.error");
				}
			}
		}
		return sponsorship;
	}
	
	public Collection<Sponsorship> sponsorshipsPerFilm(int filmId) {
		Collection<Sponsorship> res = new ArrayList<>();
		
		res = this.sponsorshipRepository.sponsorshipsPerFilm(filmId);
		
		return res;
	}
	
	public Collection<Sponsorship> sponsorshipsPerSponsor(int sponsorId) {
		Collection<Sponsorship> res = new ArrayList<>();
		
		res = this.sponsorshipRepository.sponsorshipsPerSponsor(sponsorId);
		
		return res;
	}
	
	public Collection<Sponsorship> sponsorshipsToReview() {
		Collection<Sponsorship> res = new ArrayList<>();
		
		res = this.sponsorshipRepository.sponsorshipsToReview();
		
		return res;
	}
}

