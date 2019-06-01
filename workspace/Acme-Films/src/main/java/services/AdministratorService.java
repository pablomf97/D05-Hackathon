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

import repositories.AdministratorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.Message;
import domain.MessageBox;
import domain.SocialProfile;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Transactional
@Service
public class AdministratorService {

	/* Working repository */

	@Autowired
	private AdministratorRepository administratorRepository;

	/* Services */

	@Autowired
	private ActorService actorService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private MessageService messageService;
	@Autowired
	private MessageBoxService messageBoxService;
	
	@Autowired
	private SocialProfileService profileService;
	
	
	

	/* Simple CRUD methods */

	/**
	 * Create an administrator
	 * 
	 * @return Administrator
	 */
	public Administrator create() {
		Administrator res;

		Collection<SocialProfile> social;
		social = new ArrayList<>();

		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authority;

		auth = new Authority();
		authority = new ArrayList<Authority>();
		userAccount = new UserAccount();
		res = new Administrator();

		auth.setAuthority(Authority.ADMIN);
		authority.add(auth);
		userAccount.setAuthorities(authority);

		res.setUserAccount(userAccount);
		res.setSocialProfile(social);

		return res;
	}

	/**
	 * Find an administrator on the database
	 * 
	 * @param administratorId
	 * 
	 * @return Administrator
	 */
	public Administrator findOne(final Integer administratorId) {
		Administrator res;

		Assert.notNull(administratorId);
		res = this.administratorRepository.findOne(administratorId);

		return res;
	}

	/**
	 * Find all administrators
	 * 
	 * @return Collection<Administrator>
	 */
	public List<Administrator> findAll() {
		return this.administratorRepository.findAll();
	}

	/**
	 * Save an administrator
	 * 
	 * @param Administator
	 * 
	 * @return Administrator
	 */
	public Administrator save(final Administrator administrator) {
		Administrator res;
		Actor principal;

		Assert.notNull(administrator);

		principal = this.actorService.findByPrincipal();

		if (administrator.getId() == 0) {

			Assert.isTrue(this.actorService.checkAuthority(principal, "ADMIN"),
					"no.permission");

			/* Managing phone number */
			final char[] phoneArray = administrator.getPhoneNumber()
					.toCharArray();
			if ((!administrator.getPhoneNumber().equals(null) && !administrator
					.getPhoneNumber().equals("")))
				if (phoneArray[0] != '+' && Character.isDigit(phoneArray[0])) {
					final String cc = this.systemConfigurationService
							.findMySystemConfiguration().getCountryCode();
					administrator.setPhoneNumber(cc + " "
							+ administrator.getPhoneNumber());
				}
		} else {

			Assert.isTrue(principal.getId() == administrator.getId(),
					"no.permission");

			administrator.setUserAccount(principal.getUserAccount());
		}
		res = this.administratorRepository.save(administrator);
		return res;
	}

	/**
	 * Delete an administrator
	 * 
	 * @param Administator
	 */
	public void delete(final Administrator administrator) {
		Actor principal;

		Assert.notNull(administrator);

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(principal.getId() == administrator.getId(),
				"no.permission");

		this.administratorRepository.delete(administrator.getId());
	}

	/**
	 * Reconstruct an administrator from a register object form from the
	 * database
	 * 
	 * @param RegisterFormObject
	 * 
	 * @return Administrator
	 */
	public Administrator reconstruct(final RegisterFormObject form,
			final BindingResult binding) {

		/* Creating admin */
		final Administrator res = this.create();

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
		authority.setAuthority(Authority.ADMIN);
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
				Assert.isTrue(
						this.actorService.checkEmail(form.getEmail(), "ADMIN"),
						"actor.email.error");

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
	 * Reconstruct an administrator from the database
	 * 
	 * @param Administator
	 * 
	 * @return Administrator
	 */
	public Administrator reconstruct(final EditionFormObject form,
			final BindingResult binding) {

		Actor principal = this.actorService.findByPrincipal();

		/* Creating admin */
		final Administrator res = this.create();

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
				Assert.isTrue(
						this.actorService.checkEmail(form.getEmail(), "ADMIN"),
						"actor.email.error");

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

	public Administrator findByUsername(final String username) {
		return this.administratorRepository.findByUsername(username);
	}

	public void flush() {
		this.administratorRepository.flush();
	}
	
	public void deleteAdmin(Administrator a){
		
	//	this.messageService.deleteMessagesInvolved(a.getId());
		//this.messageBoxService.deleteBoxes(a.getId());
		//borro todos los mensajes donde sea receiver del sistema
		
		Collection<MessageBox> boxesPrincipal=this.messageBoxService.findByOwner(a.getId());
		
		Collection<MessageBox> boxesReceiver=this.messageBoxService.findByOwner(a.getId());

		
		//SocialProfiles:
		for(SocialProfile s : a.getSocialProfile()){
			this.profileService.delete(s);	
		}
	
		
		
		this.delete(a);
	}
	
	
	
}
