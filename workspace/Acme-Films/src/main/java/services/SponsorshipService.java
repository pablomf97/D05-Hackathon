package services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

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
	private SystemConfigurationService sysConfigService;
	
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
		
		Assert.isTrue(this.actorService.checkAuthority(principal, "MODERATOR") || this.actorService.checkAuthority(principal, "SPONSOR") ,
				"not.allowed");
		
		if(sponsorship.getId() != 0) {
			if (this.actorService.checkAuthority(principal, "SPONSOR")) {
				Assert.isTrue(sponsorship.getSponsor().equals((Sponsor) (principal)), "not.allowed");
			}
		}
		
		for(Film film : sponsorship.getFilms()) {
			Assert.isTrue(!film.getIsDraft());
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
		
		try {
			Assert.notNull(form.getFilms(), "no.films");
		} catch (Throwable oops) {
			binding.rejectValue("films", "films.error");
		}
		
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
		
		Sponsorship sponsorship = this.create();

		/* Creating sponsorship */
		Sponsorship aux = this.findOne(form.getId());

//		sponsorship.setId(form.getId());
//		sponsorship.setVersion(form.getVersion());
		
		sponsorship.setId(aux.getId());
		sponsorship.setVersion(aux.getVersion());
		sponsorship.setIsActive(aux.getIsActive());
		
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
		
		//this.validator.validate(sponsorship, binding);
		
		try {
			Assert.notNull(form.getFilms(), "no.films");
		} catch (Throwable oops) {
			binding.rejectValue("films", "films.error");
		}

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
	

		public Collection <Sponsorship> sponsorshipPerSponsor(int id){
			return this.sponsorshipRepository.sponsorshipPerSponsor(id);
		}
		
		public void deleteSponsorships(int id){
			
			this.sponsorshipRepository.deleteInBatch(this.sponsorshipPerSponsor(id));
		}

		public void deleteSponsorshipsPerFilms(Film f) {
			
			for(Sponsorship s :this.findAll()){
				
				if(s.getFilms().contains(f)){
					this.sponsorshipRepository.delete(s);
					
				}
			}
			
			
			
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
	
	public Sponsorship findBanner(int filmId) {
		Sponsorship result = new Sponsorship();
		Collection<Sponsorship> sponsorships = this.findSponsorshipsByFilm(filmId);
		
		if(!sponsorships.isEmpty()) {
			result = this.randomBanner(sponsorships);
		}
		
		return result;
	}
	
	public Collection<Sponsorship> findSponsorshipsByFilm(int filmId) {
		Collection<Sponsorship> result = this.sponsorshipRepository.sponsorshipsPerFilm(filmId);
		
		return result;
	}
	
	public Sponsorship randomBanner(final Collection<Sponsorship> sponsorships) {
		Sponsorship result;
		final SecureRandom rnd = new SecureRandom();
		final List<Sponsorship> listSponsoships = new ArrayList<>(sponsorships);
		final List<Sponsorship> listAux = new ArrayList<>(listSponsoships);

		for (final Sponsorship s : listAux)
			try {
				if (this.creditCardService.checkIfExpired(s.getCreditCard().getExpirationMonth(), s.getCreditCard().getExpirationYear()))
					listSponsoships.remove(s);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}

		Integer a = (rnd.nextInt() % 10);
		while (a < 0 || a > (sponsorships.size() - 1))
			a = (rnd.nextInt() % 10);
		result = listSponsoships.get(a);
		
		this.receivePaymentForSponsorship(result);

		return result;
	}
	
	private void receivePaymentForSponsorship(Sponsorship sponsorship) {
		final Double tax = this.sysConfigService.findMySystemConfiguration().getVATTax();
		final Double flatRate = this.sysConfigService.findMySystemConfiguration().getVATTax();
		final Double paymentQuantity = flatRate + (flatRate*tax);
		CreditCard sponsorCreditCard = sponsorship.getCreditCard();
		
		//Here would be our api call to the selected payment method

	}

}

