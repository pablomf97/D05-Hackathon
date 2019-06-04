package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Actor;
import domain.Moderator;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ModeratorServiceTest extends AbstractTest {

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%):
	 * 
	 * 
	 * Coverage of the total project (lines of codes):
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%): 6,5
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 2.326
	 */

	// System under test ---------------------------------------

	@Autowired
	private ModeratorService moderatorService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	// Tests ----------------------------------------------------

	// Test: Caso de uso:
	// An actor who is not authenticated must be able to:
	// Register to the system as a moderator. (7.1)
	@Test
	public void driverRegister() {
		Object registerTestingData[][] = {
				/* Positive case */
				{ "admin", "moderatorT", "moderatorT", "moderatorT",
						"moderatorT", "https://www.foto.com",
						"moderatorT@moderatorT.moderatorT", "666666666",
						"c/ moderatorT", null },
				/* Negative: invalid moderator */
				{ null, "moderatorT1", "moderatorT", "moderatorT",
						"moderatorT", "https://www.foto.com",
						"moderatorT@moderatorT.moderatorT", "666666666",
						"c/ moderatorT", IllegalArgumentException.class },
				/* Negative cases: invalid data */
				{ "admin", "moderatorT2", "", "moderatorT", "moderatorT",
						"https://www.foto.com", "moderatorT@", "666666666",
						"c/ moderatorT", IllegalArgumentException.class },
				{ "admin", "moderatorT3", "moderatorT", "", "",
						"https://www.foto.com", "moderatorT@", "666666666",
						"c/ moderatorT", IllegalArgumentException.class },
				{ "admin", "moderatorT4", "moderatorT", "moderatorT",
						"moderatorT", "https://www.foto.com", null,
						"666666666", "c/ moderatorT",
						IllegalArgumentException.class },
				{ "admin", "moderatorT", "moderatorT", "moderatorT",
						"moderatorT", "https://www.foto.com", "moderatorT@",
						"666666666", "c/ moderatorT",
						IllegalArgumentException.class } };

		for (int i = 0; i < registerTestingData.length; i++) {
			templateRegister((String) registerTestingData[i][0],
					(String) registerTestingData[i][1],
					(String) registerTestingData[i][2],
					(String) registerTestingData[i][3],
					(String) registerTestingData[i][4],
					(String) registerTestingData[i][5],
					(String) registerTestingData[i][6],
					(String) registerTestingData[i][7],
					(String) registerTestingData[i][8],
					(Class<?>) registerTestingData[i][9]);
		}
	}

	protected void templateRegister(String creatorUsername, String username,
			String password, String name, String surname, String photo,
			String email, String phoneNumber, String address, Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			authenticate(creatorUsername);

			this.registerModerator(username, password, name, surname, photo,
					email, phoneNumber, address);

			unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void registerModerator(String username, String password,
			String name, String surname, String photo, String email,
			String phoneNumber, String address) {

		RegisterFormObject moderatorForm = new RegisterFormObject();
		Moderator newAdmin = new Moderator();

		moderatorForm.setUsername(username);
		moderatorForm.setPassword(password);
		moderatorForm.setPassConfirmation(password);
		moderatorForm.setTermsAndConditions(true);
		moderatorForm.setName(name);
		moderatorForm.setSurname(surname);
		moderatorForm.setPhoto(photo);
		moderatorForm.setEmail(email);
		moderatorForm.setPhoneNumber(phoneNumber);
		moderatorForm.setAddress(address);

		newAdmin = this.reconstructNoBinding(moderatorForm);

		this.moderatorService.save(newAdmin);
	}

	private Moderator reconstructNoBinding(RegisterFormObject form) {
		final Moderator res = this.moderatorService.create();

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
		Assert.isTrue(form.getPassword().equals(form.getPassConfirmation()),
				"pass.confirm.error");
		Assert.isTrue(form.getPassword() != null
				&& !form.getPassword().isEmpty());

		/* Terms&Conditions */
		Assert.isTrue((form.getTermsAndConditions()), "terms.error");

		Assert.isTrue(form.getEmail() != null && !form.getEmail().isEmpty());
		Assert.isTrue(
				this.actorService.checkEmail(form.getEmail(), "MODERATOR"),
				"actor.email.error");

		/* Username */
		Assert.isTrue(this.actorService.existsUsername(form.getUsername()),
				"username.error");

		Assert.isTrue(form.getSurname() != null && !form.getSurname().isEmpty());
		Assert.isTrue(form.getName() != null && !form.getName().isEmpty());

		if (form.getPhoneNumber() != null) {
			final char[] phoneArray = form.getPhoneNumber().toCharArray();
			if ((!form.getPhoneNumber().equals(null) && !form.getPhoneNumber()
					.equals("")))
				if (phoneArray[0] != '+' && Character.isDigit(phoneArray[0])) {
					final String cc = this.systemConfigurationService
							.findMySystemConfiguration().getCountryCode();
					form.setPhoneNumber(cc + " " + form.getPhoneNumber());
				}
		}

		return res;
	}

	/*
	 * ########################################################################
	 */

	@Test
	public void driverEdit() {
		Object editionTestingData[][] = {
				/* Positive case */
				{ "moderator1", "moderator1moderator1", "moderator1",
						"https://www.foto.com",
						"moderator@moderator1.moderator1", "666666666",
						"c/ moderator", null },
				/* Negative cases: invalid data */
				{ "", "moderator1", "moderator1", "https://www.foto.com",
						"moderator@moderator.moderator", "666666666",
						"c/ moderator", IllegalArgumentException.class },
				{ "moderator1", "moderator", "moderator",
						"https://www.foto.com", "moderator@", "666666666",
						"c/ moderator", IllegalArgumentException.class },
				{ "moderator1", "", "", "https://www.foto.com", "moderator@",
						"666666666", "c/ moderator",
						IllegalArgumentException.class },
				{ "moderator1", "moderator", "moderator",
						"https://www.foto.com", "", "666666666",
						"c/ moderator", IllegalArgumentException.class } };

		for (int i = 0; i < editionTestingData.length; i++) {
			templateEdit((String) editionTestingData[i][0],
					(String) editionTestingData[i][1],
					(String) editionTestingData[i][2],
					(String) editionTestingData[i][3],
					(String) editionTestingData[i][4],
					(String) editionTestingData[i][5],
					(String) editionTestingData[i][6],
					(Class<?>) editionTestingData[i][7]);
		}
	}

	protected void templateEdit(String username, String name, String surname,
			String photo, String email, String phoneNumber, String address,
			Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			authenticate(username);

			this.editModerator(username, name, surname, photo, email,
					phoneNumber, address);

			unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void editModerator(String username, String name, String surname,
			String photo, String email, String phoneNumber, String address) {

		EditionFormObject moderatorForm = new EditionFormObject(
				this.moderatorService.findByUsername(username));
		Moderator newAdmin = new Moderator();
		BindingResult binding = null;

		moderatorForm.setUsername(username);
		moderatorForm.setName(name);
		moderatorForm.setSurname(surname);
		moderatorForm.setPhoto(photo);
		moderatorForm.setEmail(email);
		moderatorForm.setPhoneNumber(phoneNumber);
		moderatorForm.setAddress(address);

		newAdmin = this.reconstructEditionTest(moderatorForm, binding);

		this.moderatorService.save(newAdmin);
	}

	public Moderator reconstructEditionTest(final EditionFormObject form,
			final BindingResult binding) {

		Actor principal = this.actorService.findByPrincipal();

		/* Creating moderator */
		final Moderator res = this.moderatorService.create();

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

		Assert.isTrue(form.getEmail() != null && !form.getEmail().isEmpty());
		Assert.isTrue(
				this.actorService.checkEmail(form.getEmail(), "MODERATOR"),
				"actor.email.error");

		Assert.isTrue(form.getSurname() != null && !form.getSurname().isEmpty());
		Assert.isTrue(form.getName() != null && !form.getName().isEmpty());

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

}