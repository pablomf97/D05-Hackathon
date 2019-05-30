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
import domain.Administrator;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdministratorServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%):
	 * 
	 * 
	 * Coverage of the total project (lines of codes):
	 */

	// System under test ---------------------------------------

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	// Tests ----------------------------------------------------

	// Test: Caso de uso:
	// An actor who is not authenticated must be able to:
	// Register to the system as a administrator. (7.1)
	@Test
	public void driverRegister() {
		Object registerTestingData[][] = {
				/* Positive case */
				{ "admin", "adminT", "adminT", "adminT", "adminT",
						"https://www.foto.com", "adminT@", "666666666",
						"c/ adminT", null },
				/* Negative: invalid admin */
				{ null, "adminT1", "adminT", "adminT", "adminT",
						"https://www.foto.com", "adminT@adminT.adminT",
						"666666666", "c/ adminT",
						IllegalArgumentException.class },
				/* Negative cases: invalid data */
				{ "admin", "adminT2", "", "adminT", "adminT",
						"https://www.foto.com", "adminT@", "666666666",
						"c/ adminT", IllegalArgumentException.class },
				{ "admin", "adminT3", "adminT", "", "", "https://www.foto.com",
						"adminT@", "666666666", "c/ adminT",
						IllegalArgumentException.class },
				{ "admin", "adminT4", "adminT", "adminT", "adminT",
						"https://www.foto.com", null, "666666666", "c/ adminT",
						IllegalArgumentException.class },
				{ "admin", "adminT", "adminT", "adminT", "adminT",
						"https://www.foto.com", "adminT@", "666666666",
						"c/ adminT", IllegalArgumentException.class } };

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

			this.registerAdministrator(username, password, name, surname,
					photo, email, phoneNumber, address);

			unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void registerAdministrator(String username, String password,
			String name, String surname, String photo, String email,
			String phoneNumber, String address) {

		RegisterFormObject adminForm = new RegisterFormObject();
		Administrator newAdmin = new Administrator();

		adminForm.setUsername(username);
		adminForm.setPassword(password);
		adminForm.setPassConfirmation(password);
		adminForm.setTermsAndConditions(true);
		adminForm.setName(name);
		adminForm.setSurname(surname);
		adminForm.setPhoto(photo);
		adminForm.setEmail(email);
		adminForm.setPhoneNumber(phoneNumber);
		adminForm.setAddress(address);

		newAdmin = this.reconstructNoBinding(adminForm);

		this.administratorService.save(newAdmin);
	}

	private Administrator reconstructNoBinding(RegisterFormObject form) {
		final Administrator res = this.administratorService.create();

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
		Assert.isTrue(form.getPassword().equals(form.getPassConfirmation()),
				"pass.confirm.error");
		Assert.isTrue(form.getPassword() != null
				&& !form.getPassword().isEmpty());

		/* Terms&Conditions */
		Assert.isTrue((form.getTermsAndConditions()), "terms.error");

		Assert.isTrue(form.getEmail() != null && !form.getEmail().isEmpty());
		Assert.isTrue(this.actorService.checkEmail(form.getEmail(), "ADMIN"),
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
				{ "admin", "admin", "admin", "https://www.foto.com", "admin@",
						"666666666", "c/ admin", null },
				/* Negative cases: invalid data */
				{ "", "admin", "admin", "https://www.foto.com",
						"admin@admin.admin", "666666666", "c/ admin",
						IllegalArgumentException.class },
				{ "admin", "admin", "admin", "https://www.foto.com",
						"admin@admin.admin", "666666666", "c/ admin",
						IllegalArgumentException.class },
				{ "admin", "", "", "https://www.foto.com", "admin@",
						"666666666", "c/ admin", IllegalArgumentException.class },
				{ "admin", "admin", "admin", "https://www.foto.com", "",
						"666666666", "c/ admin", IllegalArgumentException.class } };

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

			this.editAdministrator(username, name, surname, photo, email,
					phoneNumber, address);

			unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void editAdministrator(String username, String name, String surname,
			String photo, String email, String phoneNumber, String address) {

		EditionFormObject adminForm = new EditionFormObject(
				this.administratorService.findByUsername(username));
		Administrator newAdmin = new Administrator();
		BindingResult binding = null;

		adminForm.setUsername(username);
		adminForm.setName(name);
		adminForm.setSurname(surname);
		adminForm.setPhoto(photo);
		adminForm.setEmail(email);
		adminForm.setPhoneNumber(phoneNumber);
		adminForm.setAddress(address);

		newAdmin = this.reconstructEditionTest(adminForm, binding);

		this.administratorService.save(newAdmin);
	}

	public Administrator reconstructEditionTest(final EditionFormObject form,
			final BindingResult binding) {

		Actor principal = this.actorService.findByPrincipal();

		/* Creating admin */
		final Administrator res = this.administratorService.create();

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
		res.setMessageBoxes(principal.getMessageBoxes());

		Assert.isTrue(form.getEmail() != null && !form.getEmail().isEmpty());
		Assert.isTrue(this.actorService.checkEmail(form.getEmail(), "ADMIN"),
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