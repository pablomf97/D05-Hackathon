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

import repositories.FilmEnthusiastRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.FilmEnthusiast;
import domain.Message;
import domain.MessageBox;
import domain.SocialProfile;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Transactional
@Service
public class FilmEnthusiastService {

	/* Working repository */

	@Autowired
	private FilmEnthusiastRepository filmEnthusiastRepository;

	/* Services */

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private GroupService groupService;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageBoxService MessageBoxService ;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;

	/* Simple CRUD methods */

	/**
	 * Create an filmEnthusiast
	 * 
	 * @return FilmEnthusiast
	 */
	public FilmEnthusiast create() {
		FilmEnthusiast res;

		Collection<SocialProfile> social;
		social = new ArrayList<>();

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new FilmEnthusiast();

		auth.setAuthority(Authority.FILMENTHUSIAST);
		authority.add(auth);
		userAccount.setAuthorities(authority);

		res.setUserAccount(userAccount);
		res.setSocialProfile(social);

		return res;
	}

	/**
	 * Find an filmEnthusiast on the database
	 * 
	 * @param filmEnthusiastId
	 * 
	 * @return FilmEnthusiast
	 */
	public FilmEnthusiast findOne(final Integer filmEnthusiastId) {
		FilmEnthusiast res;

		Assert.notNull(filmEnthusiastId);
		res = this.filmEnthusiastRepository.findOne(filmEnthusiastId);

		return res;
	}

	/**
	 * Find all filmEnthusiasts
	 * 
	 * @return Collection<FilmEnthusiast>
	 */
	public List<FilmEnthusiast> findAll() {
		return this.filmEnthusiastRepository.findAll();
	}

	/**
	 * Save an filmEnthusiast
	 * 
	 * @param Administator
	 * 
	 * @return FilmEnthusiast
	 */
	public FilmEnthusiast save(final FilmEnthusiast filmEnthusiast) {
		FilmEnthusiast res;
		FilmEnthusiast principal;

		Assert.notNull(filmEnthusiast);

		if (filmEnthusiast.getId() != 0) {
			principal = (FilmEnthusiast) this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == filmEnthusiast.getId(),
					"no.permission");

			filmEnthusiast.setUserAccount(principal.getUserAccount());
			filmEnthusiast.setFinder(principal.getFinder());
		}
		res = this.filmEnthusiastRepository.save(filmEnthusiast);
		return res;
	}

	/**
	 * Delete an filmEnthusiast
	 * 
	 * @param Administator
	 */
	public void delete(final FilmEnthusiast filmEnthusiast) {
		Actor principal;

		Assert.notNull(filmEnthusiast);

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(principal.getId() == filmEnthusiast.getId(),
				"no.permission");

		this.filmEnthusiastRepository.delete(filmEnthusiast.getId());
	}

	/**
	 * Reconstruct an filmEnthusiast from a register object form from the
	 * database
	 * 
	 * @param RegisterFormObject
	 * 
	 * @return FilmEnthusiast
	 */
	public FilmEnthusiast reconstruct(final RegisterFormObject form,
			final BindingResult binding) {

		/* Creating admin */
		final FilmEnthusiast res = this.create();

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
		authority.setAuthority(Authority.FILMENTHUSIAST);
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
						"FILMENTHUSIAST"), "actor.email.error");

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
	 * Reconstruct an filmEnthusiast from the database
	 * 
	 * @param Administator
	 * 
	 * @return FilmEnthusiast
	 */
	public FilmEnthusiast reconstruct(final EditionFormObject form,
			final BindingResult binding) {

		Actor principal = this.actorService.findByPrincipal();

		/* Creating admin */
		final FilmEnthusiast res = this.create();

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
						"FILMENTHUSIAST"), "actor.email.error");

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

	public FilmEnthusiast findByUsername(final String username) {
		return this.filmEnthusiastRepository.findByUsername(username);
	}

	public void flush() {
		this.filmEnthusiastRepository.flush();
	}
	
	public void deleteFilmEnthusiast(FilmEnthusiast f){
		
		//this.groupService.deleteGroupPerFilmEnthusiast(f);
	//	this.eventService.deleteEventPerFilmEnthusiast(f);
		
		for(Message m :this.messageService.messagesInvolved(f.getId())){
			for(MessageBox mb:this.MessageBoxService.findAll()){
				
				if(mb.getMessages().contains(m)){
					mb.getMessages().remove(m);
				}
				
			}
			
			this.messageService.deleteMessage(m);
		}
		
		for(MessageBox mb:this.MessageBoxService.findAll()){
			
			if(mb.getOwner()==f){
				this.MessageBoxService.deleteBox(mb);
			}
		}

		
		this.delete(f);
	}
	
}
