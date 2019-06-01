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
import domain.Film;
import domain.Sponsor;
import domain.Sponsorship;

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
		result.setIsActive(false);
		
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
		Assert.isTrue(this.actorService.checkAuthority(principal, "SPONSOR"), "not.allowed");
		
		Assert.isTrue(principal.getId() == sponsorship.getSponsor().getId());
		
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
		Assert.isTrue(this.actorService.checkAuthority(principal, "SPONSOR"),
				"not.allowed");

		this.sponsorshipRepository.delete(sponsorship.getId());
	}
	
	// Other business methods -------------------------------
	
	public Sponsorship reconstruct(final Sponsorship sponsorship,
			 BindingResult binding) {
		Sponsorship result;
		Actor principal;
		
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "SPONSOR"),
				"not.allowed");
		
		Assert.isTrue(principal.getId() == sponsorship.getSponsor().getId());
		
		Assert.notNull(sponsorship.getTitle());
		Assert.notNull(sponsorship.getBanner());
		Assert.notNull(sponsorship.getTargetPage());
		Assert.notNull(sponsorship.getCreditCard());
		Assert.notNull(sponsorship.getSponsor());
		
		if (sponsorship.getId() == 0) {
			result = this.create();
			
			result.setBanner(sponsorship.getBanner());
			result.setTargetPage(sponsorship.getTargetPage());
			result.setTitle(sponsorship.getTitle());
			result.setFilms(sponsorship.getFilms());
			
		} else {
			result = this.findOne(sponsorship.getId());
			
			if(!sponsorship.getIsActive()) {
				
				result.setBanner(sponsorship.getBanner());
				result.setTargetPage(sponsorship.getTargetPage());
				result.setTitle(sponsorship.getTitle());
				result.setFilms(sponsorship.getFilms());
			}
		}
		
		result.setCreditCard(sponsorship.getCreditCard());

		this.validator.validate(result, binding);
		
		return result;
	}
	
	public Collection<Sponsorship> sponsorshipsPerFilm(int filmId) {
		Collection<Sponsorship> res = new ArrayList<>();
		
		res = this.sponsorshipRepository.sponsorshipsPerFilm(filmId);
		
		return res;
	}
	
		public Collection <Sponsorship> sponsorshipPerSponsor(int id){
			return this.sponsorshipRepository.sponsorshipPerSponsor(id);
		}
}

