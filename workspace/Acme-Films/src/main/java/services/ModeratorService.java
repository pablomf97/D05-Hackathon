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

import repositories.ModeratorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Message;
import domain.MessageBox;
import domain.Moderator;
import domain.SocialProfile;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Transactional
@Service
public class ModeratorService {

	/* Working repository */

	@Autowired
	private ModeratorRepository moderatorRepository;

	/* Services */

	@Autowired
	private ActorService actorService;
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@Autowired
	private FilmService filmService;

	@Autowired
	private GroupService groupService;
	
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageBoxService MessageBoxService ;


	/* Simple CRUD methods */

	/**
	 * Create an moderator
	 * 
	 * @return Moderator
	 */
	public Moderator create() {
		Moderator res;

		Collection<SocialProfile> social;
		social = new ArrayList<>();

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Moderator();

		auth.setAuthority(Authority.MODERATOR);
		authority.add(auth);
		userAccount.setAuthorities(authority);

		res.setUserAccount(userAccount);
		res.setSocialProfile(social);

		return res;
	}

	/**
	 * Find an moderator on the database
	 * 
	 * @param moderatorId
	 * 
	 * @return Moderator
	 */
	public Moderator findOne(final Integer moderatorId) {
		Moderator res;

		Assert.notNull(moderatorId);
		res = this.moderatorRepository.findOne(moderatorId);

		return res;
	}

	/**
	 * Find all moderators
	 * 
	 * @return Collection<Moderator>
	 */
	public List<Moderator> findAll() {
		return this.moderatorRepository.findAll();
	}

	/**
	 * Save an moderator
	 * 
	 * @param Administator
	 * 
	 * @return Moderator
	 */
	public Moderator save(final Moderator moderator) {
		Moderator res;
		Actor principal;

		Assert.notNull(moderator);

		principal = this.actorService.findByPrincipal();

		if (moderator.getId() == 0) {

			Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"),
					"no.permission");

			res = this.moderatorRepository.save(moderator);

			this.MessageBoxService.initializeDefaultBoxes(res);
		} else {

			Assert.isTrue(principal.getId() == moderator.getId(),
					"no.permission");

			moderator.setUserAccount(principal.getUserAccount());
			res = this.moderatorRepository.save(moderator);
		}

		return res;
	}

	/**
	 * Delete an moderator
	 * 
	 * @param Administator
	 */
	public void delete(final Moderator moderator) {
		Actor principal;

		Assert.notNull(moderator);

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(principal.getId() == moderator.getId(), "no.permission");

		this.moderatorRepository.delete(moderator.getId());
	}

	/**
	 * Reconstruct an moderator from a register object form from the database
	 * 
	 * @param RegisterFormObject
	 * 
	 * @return Moderator
	 */
	public Moderator reconstruct(final RegisterFormObject form,
			final BindingResult binding) {

		/* Creating admin */
		final Moderator res = this.create();

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
		authority.setAuthority(Authority.MODERATOR);
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
						"MODERATOR"), "actor.email.error");

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
	 * Reconstruct an moderator from the database
	 * 
	 * @param Administator
	 * 
	 * @return Moderator
	 */
	public Moderator reconstruct(final EditionFormObject form,
			final BindingResult binding) {

		Actor principal = this.actorService.findByPrincipal();

		/* Creating admin */
		final Moderator res = this.create();

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
						"MODERATOR"), "actor.email.error");

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

	public Moderator findByUsername(final String username) {
		return this.moderatorRepository.findByUsername(username);
	}

	public void flush() {
		this.moderatorRepository.flush();
	}

	public Double[] statsReviewsPerModerator() {
		return this.moderatorRepository.statsReviewsPerModerator();
	}

	public void DeleteModerator(Moderator c) {

		this.reviewService.deleteReviewPerModerator(c.getId());
		this.filmService.deleteFilms(c.getId());

		
		for(Message m :this.messageService.messagesInvolved(c.getId())){
			for(MessageBox mb:this.MessageBoxService.findAll()){
				
				if(mb.getMessages().contains(m)){
					mb.getMessages().remove(m);
				}
				
			}
			
			this.messageService.deleteMessage(m);
		}
		
		for(MessageBox mb:this.MessageBoxService.findAll()){
			
			if(mb.getOwner()==c){
				this.MessageBoxService.deleteBox(mb);
			}
		}
		

		this.delete(c);

	}
}
