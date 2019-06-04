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

import repositories.CriticRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Critic;
import domain.Curricula;
import domain.Message;
import domain.MessageBox;
import domain.SocialProfile;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Transactional
@Service
public class CriticService {

	/* Working repository */

	@Autowired
	private CriticRepository criticRepository;

	/* Services */

	@Autowired
	private ActorService actorService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	@Autowired
	private ReviewService reviewService;
	
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageBoxService MessageBoxService ;

	@Autowired
	private MessageBoxService messageBoxService;

	/* Simple CRUD methods */

	/**
	 * Create an critic
	 * 
	 * @return Critic
	 */
	public Critic create() {
		Critic res;

		Collection<SocialProfile> social;
		social = new ArrayList<>();

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Critic();

		auth.setAuthority(Authority.CRITIC);
		authority.add(auth);
		userAccount.setAuthorities(authority);

		res.setUserAccount(userAccount);
		res.setSocialProfile(social);

		return res;
	}

	/**
	 * Find an critic on the database
	 * 
	 * @param criticId
	 * 
	 * @return Critic
	 */
	public Critic findOne(final Integer criticId) {
		Critic res;

		Assert.notNull(criticId);
		res = this.criticRepository.findOne(criticId);

		return res;
	}

	/**
	 * Find all critics
	 * 
	 * @return Collection<Critic>
	 */
	public List<Critic> findAll() {
		return this.criticRepository.findAll();
	}

	/**
	 * Save an critic
	 * 
	 * @param Administator
	 * 
	 * @return Critic
	 */
	public Critic save(final Critic critic) {
		Critic res;
		Critic principal;

		Assert.notNull(critic);

		if (critic.getId() != 0) {
			principal = (Critic) this.actorService.findByPrincipal();
			Assert.isTrue(principal.getId() == critic.getId(), "no.permission");

			critic.setUserAccount(principal.getUserAccount());
			critic.setCurricula(principal.getCurricula());
			res = this.criticRepository.save(critic);
		} else {
			res = this.criticRepository.save(critic);
			this.messageBoxService.initializeDefaultBoxes(res);
		}

		return res;
	}

	/**
	 * Delete an critic
	 * 
	 * @param Administator
	 */
	public void delete(final Critic critic) {
		Actor principal;

		Assert.notNull(critic);

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(principal.getId() == critic.getId(), "no.permission");

		this.criticRepository.delete(critic.getId());
	}

	/**
	 * Reconstruct an critic from a register object form from the database
	 * 
	 * @param RegisterFormObject
	 * 
	 * @return Critic
	 */
	public Critic reconstruct(final RegisterFormObject form,
			final BindingResult binding) {

		/* Creating admin */
		final Critic res = this.create();

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
		authority.setAuthority(Authority.CRITIC);
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

		if (form.getEmail() != null)
			try {
				Assert.isTrue(
						this.actorService.checkEmail(form.getEmail(), "CRITIC"),
						"actor.email.error");

			} catch (final Throwable oops) {
				binding.rejectValue("email", "email.error");
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

		if (form.getPhoneNumber() != null)
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
			} catch (final Throwable oops) {
				binding.rejectValue("phoneNumber", "phone.error");
			}

		return res;
	}

	/**
	 * Reconstruct an critic from the database
	 * 
	 * @param Administator
	 * 
	 * @return Critic
	 */
	public Critic reconstruct(final EditionFormObject form,
			final BindingResult binding) {

		final Actor principal = this.actorService.findByPrincipal();

		/* Creating admin */
		final Critic res = this.create();

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

		if (form.getEmail() != null)
			try {
				Assert.isTrue(
						this.actorService.checkEmail(form.getEmail(), "CRITIC"),
						"actor.email.error");

			} catch (final Throwable oops) {
				binding.rejectValue("email", "email.error");
			}

		/* Managing phone number */
		if (form.getPhoneNumber() != null)
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
			} catch (final Throwable oops) {
				binding.rejectValue("phoneNumber", "phone.error");
			}

		return res;
	}

	public void newCurricula(final Curricula c) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC"));
		final Critic cr = (Critic) principal;
		Assert.isNull(cr.getCurricula());
		cr.setCurricula(c);
		this.criticRepository.save(cr);
	}

	public Critic findByUsername(final String username) {
		return this.criticRepository.findByUsername(username);
	}

	public void flush() {
		this.criticRepository.flush();
	}

	public Collection<Critic> top3CriticsMoreProfessional() {

		List<Critic> l = (List<Critic>) this.criticRepository
				.top3CriticsMoreProfessional();

		if(l.size()<4){

			return l;
		} else {

			return l.subList(0, 3);
		}
	} 

	public Collection<Critic> criticsWithHighestRatingReview() {
		return this.criticRepository.criticsWithHighestRatingReview();
	}

	public void DeleteCritic(Critic c) {

		this.reviewService.deleteReviewsCritics(c.getId());
		
		
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
