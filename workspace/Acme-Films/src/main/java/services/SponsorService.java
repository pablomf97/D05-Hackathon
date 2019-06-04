package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.SponsorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.SocialProfile;
import domain.Sponsor;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Transactional
@Service
public class SponsorService {

	/* Working repository */

	@Autowired
	private SponsorRepository sponsorRepository;

	/* Services */

	@Autowired
	private ActorService actorService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@Autowired
	private SponsorshipService sponsorshipService;

	@Autowired
	private MessageBoxService messageBoxService;

	/* Simple CRUD methods */

	/**
	 * Create an sponsor
	 * 
	 * @return Sponsor
	 */
	public Sponsor create() {
		Sponsor res;

		Collection<SocialProfile> social;
		social = new ArrayList<>();

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Sponsor();

		auth.setAuthority(Authority.SPONSOR);
		authority.add(auth);
		userAccount.setAuthorities(authority);

		res.setUserAccount(userAccount);
		res.setSocialProfile(social);

		return res;
	}

	/**
	 * Find an sponsor on the database
	 * 
	 * @param sponsorId
	 * 
	 * @return Sponsor
	 */
	public Sponsor findOne(final Integer sponsorId) {
		Sponsor res;

		Assert.notNull(sponsorId);
		res = this.sponsorRepository.findOne(sponsorId);

		return res;
	}

	/**
	 * Find all sponsors
	 * 
	 * @return Collection<Sponsor>
	 */
	public List<Sponsor> findAll() {
		return this.sponsorRepository.findAll();
	}

	/**
	 * Save an sponsor
	 * 
	 * @param Administator
	 * 
	 * @return Sponsor
	 */
	public Sponsor save(final Sponsor sponsor) {
		Sponsor res;
		Actor principal;

		Assert.notNull(sponsor);

		if (sponsor.getId() != 0) {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == sponsor.getId(), "no.permission");

			sponsor.setUserAccount(principal.getUserAccount());

			res = this.sponsorRepository.save(sponsor);
		} else {
			res = this.sponsorRepository.save(sponsor);
			this.messageBoxService.initializeDefaultBoxes(res);
		}
		return res;
	}

	/**
	 * Delete an sponsor
	 * 
	 * @param Administator
	 */
	public void delete(final Sponsor sponsor) {
		Actor principal;

		Assert.notNull(sponsor);

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(principal.getId() == sponsor.getId(), "no.permission");

		this.sponsorRepository.delete(sponsor.getId());
	}

	/**
	 * Reconstruct an sponsor from a register object form from the database
	 * 
	 * @param RegisterFormObject
	 * 
	 * @return Sponsor
	 */
	public Sponsor reconstruct(final RegisterFormObject form,
			final BindingResult binding) {

		/* Creating admin */
		final Sponsor res = this.create();

		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());

		/* Creating user account */
		final UserAccount userAccount = new UserAccount();

		final List<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.SPONSOR);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		userAccount.setUsername(form.getUsername());

		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		userAccount
				.setPassword(encoder.encodePassword(form.getPassword(), null));

		res.setUserAccount(userAccount);

		/* Password confirmation */
		if (form.getPassword() != null)
			try {
				Assert.isTrue(
						form.getPassword().equals(form.getPassConfirmation()),
						"pass.confirm.error");
			} catch (final Throwable oops) {
				binding.rejectValue("password", "pass.confirm.error");
			}

		/* Terms&Conditions */
		if (form.getTermsAndConditions() != null)
			try {
				Assert.isTrue((form.getTermsAndConditions()), "terms.error");
			} catch (final Throwable oops) {
				binding.rejectValue("termsAndConditions", "terms.error");
			}

		if (form.getEmail() != null) {
			try {
				Assert.isTrue(this.actorService.checkEmail(form.getEmail(),
						"SPONSOR"), "actor.email.error");

			} catch (Throwable oops) {
				binding.rejectValue("email", "email.error");
			}
		}

		/* Username */
		if (form.getUsername() != null)
			try {
				Assert.isTrue(
						this.actorService.existsUsername(form.getUsername()),
						"username.error");
			} catch (final Throwable oops) {
				binding.rejectValue("username", "username.error");
			}

		if (form.getPhoneNumber() != null) {
			try {
				final char[] phoneArray = form.getPhoneNumber().toCharArray();
				if ((!form.getPhoneNumber().equals(null) && !form
						.getPhoneNumber().equals("")))
					if (phoneArray[0] != '+'
							&& Character.isDigit(phoneArray[0])) {
						final String cc = this.systemConfigurationService
								.findMySystemConfiguration().getCountryCode();
						form.setPhoneNumber(cc + " " + form.getPhoneNumber());
					}
			} catch (Throwable oops) {
				binding.rejectValue("phoneNumber", "phone.error");
			}
		}

		return res;
	}

	/**
	 * Reconstruct an sponsor from the database
	 * 
	 * @param Administator
	 * 
	 * @return Sponsor
	 */
	public Sponsor reconstruct(final EditionFormObject form,
			final BindingResult binding) {

		Actor principal = this.actorService.findByPrincipal();

		/* Creating admin */
		final Sponsor res = this.create();

		res.setId(form.getId());
		res.setVersion(form.getVersion());
		res.setName(form.getName());
		res.setSurname(form.getSurname());
		res.setPhoto(form.getPhoto());
		res.setEmail(form.getEmail());
		res.setPhoneNumber(form.getPhoneNumber());
		res.setAddress(form.getAddress());
		res.setIsSpammer(principal.getIsSpammer());
		res.setSocialProfile(principal.getSocialProfile());

		if (form.getEmail() != null) {
			try {
				Assert.isTrue(this.actorService.checkEmail(form.getEmail(),
						"SPONSOR"), "actor.email.error");

			} catch (Throwable oops) {
				binding.rejectValue("email", "email.error");
			}
		}

		/* Managing phone number */
		if (form.getPhoneNumber() != null) {
			try {
				final char[] phoneArray = form.getPhoneNumber().toCharArray();
				if ((!form.getPhoneNumber().equals(null) && !form
						.getPhoneNumber().equals("")))
					if (phoneArray[0] != '+'
							&& Character.isDigit(phoneArray[0])) {
						final String cc = this.systemConfigurationService
								.findMySystemConfiguration().getCountryCode();
						form.setPhoneNumber(cc + " " + form.getPhoneNumber());
					}
			} catch (Throwable oops) {
				binding.rejectValue("phoneNumber", "phone.error");
			}
		}

		return res;
	}

	public Sponsor findByUsername(final String username) {
		return this.sponsorRepository.findByUsername(username);
	}

	public void flush() {
		this.sponsorRepository.flush();
	}

	public Double[] statsSponsorshipsPerSponsor() {
		return this.sponsorRepository.statsSponsorshipsPerSponsor();
	}

	public void deleteSponsor(Sponsor c) {

		// Sponsorships
		this.sponsorshipService.deleteSponsorships(c.getId());

		this.delete(c);
	}
}
