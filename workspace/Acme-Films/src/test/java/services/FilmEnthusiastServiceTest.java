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
import domain.FilmEnthusiast;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FilmEnthusiastServiceTest extends AbstractTest {

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
	 * Coverage of the total project (%): 6,3
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 2.275
	 */

	// System under test ---------------------------------------

	@Autowired
	private FilmEnthusiastService filmEnthusiastService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	// Tests ----------------------------------------------------

	// Test: Caso de uso:
	// An actor who is not authenticated must be able to:
	// Register to the system as a filmEnthusiast. (7.1)
	@Test
	public void driverRegister() {
		Object registerTestingData[][] = {
				/* Positive case */
				{ "filmEnthusiastT", "filmEnthusiastT", "filmEnthusiastT",
						"filmEnthusiastT", "https://www.foto.com",
						"filmEnthusiastT@filmEnthusiastT.filmEnthusiastT",
						"666666666", "c/ filmEnthusiastT", null },
				/* Negative: invalid filmEnthusiast */
				{ "", "filmEnthusiastT", "filmEnthusiastT", "filmEnthusiastT",
						"https://www.foto.com",
						"filmEnthusiastT@filmEnthusiastT.filmEnthusiastT",
						"666666666", "c/ filmEnthusiastT",
						IllegalArgumentException.class },
				/* Negative cases: invalid data */
				{ "filmEnthusiastT2", "", "filmEnthusiastT", "filmEnthusiastT",
						"https://www.foto.com", "filmEnthusiastT@",
						"666666666", "c/ filmEnthusiastT",
						IllegalArgumentException.class },
				{ "filmEnthusiastT3", "filmEnthusiastT", "", "",
						"https://www.foto.com", "filmEnthusiastT@",
						"666666666", "c/ filmEnthusiastT",
						IllegalArgumentException.class },
				{ "filmEnthusiastT4", "filmEnthusiastT", "filmEnthusiastT",
						"filmEnthusiastT", "https://www.foto.com", null,
						"666666666", "c/ filmEnthusiastT",
						IllegalArgumentException.class },
				{ "filmEnthusiastT5", "filmEnthusiastT", "filmEnthusiastT",
						"filmEnthusiastT", "https://www.foto.com",
						"filmEnthusiastT@", "666666666", "c/ filmEnthusiastT",
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
					(Class<?>) registerTestingData[i][8]);
		}
	}

	protected void templateRegister(String username, String password,
			String name, String surname, String photo, String email,
			String phoneNumber, String address, Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.registerFilmEnthusiast(username, password, name, surname,
					photo, email, phoneNumber, address);

			unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void registerFilmEnthusiast(String username, String password,
			String name, String surname, String photo, String email,
			String phoneNumber, String address) {

		RegisterFormObject filmEnthusiastForm = new RegisterFormObject();
		FilmEnthusiast newAdmin = new FilmEnthusiast();

		filmEnthusiastForm.setUsername(username);
		filmEnthusiastForm.setPassword(password);
		filmEnthusiastForm.setPassConfirmation(password);
		filmEnthusiastForm.setTermsAndConditions(true);
		filmEnthusiastForm.setName(name);
		filmEnthusiastForm.setSurname(surname);
		filmEnthusiastForm.setPhoto(photo);
		filmEnthusiastForm.setEmail(email);
		filmEnthusiastForm.setPhoneNumber(phoneNumber);
		filmEnthusiastForm.setAddress(address);

		newAdmin = this.reconstructNoBinding(filmEnthusiastForm);

		this.filmEnthusiastService.save(newAdmin);
	}

	private FilmEnthusiast reconstructNoBinding(RegisterFormObject form) {
		final FilmEnthusiast res = this.filmEnthusiastService.create();

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
		Assert.isTrue(form.getPassword().equals(form.getPassConfirmation()),
				"pass.confirm.error");
		Assert.isTrue(form.getPassword() != null
				&& !form.getPassword().isEmpty());

		/* Terms&Conditions */
		Assert.isTrue((form.getTermsAndConditions()), "terms.error");

		Assert.isTrue(form.getEmail() != null && !form.getEmail().isEmpty());
		Assert.isTrue(
				this.actorService.checkEmail(form.getEmail(), "FILMENTHUSIAST"),
				"actor.email.error");

		/* Username */
		Assert.isTrue(form.getUsername() != null
				&& !form.getUsername().isEmpty());
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
				{ "filmEnthusiast1", "filmEnthusiast", "filmEnthusiast",
						"https://www.foto.com",
						"filmEnthusiast@filmEnthusiast.filmEnthusiast",
						"666666666", "c/ filmEnthusiast", null },
				/* Negative cases: invalid data */
				{ "", "filmEnthusiast", "filmEnthusiast",
						"https://www.foto.com",
						"filmEnthusiast@filmEnthusiast.filmEnthusiast",
						"666666666", "c/ filmEnthusiast",
						IllegalArgumentException.class },
				{ "filmEnthusiast1", "filmEnthusiast", "filmEnthusiast",
						"https://www.foto.com", "filmEnthusiast@", "666666666",
						"c/ filmEnthusiast", IllegalArgumentException.class },
				{ "filmEnthusiast1", "", "", "https://www.foto.com",
						"filmEnthusiast@filmEnthusiast1.filmEnthusiast1",
						"666666666", "c/ filmEnthusiast",
						IllegalArgumentException.class },
				{ "filmEnthusiast1", "filmEnthusiast", "filmEnthusiast",
						"https://www.foto.com", "", "666666666",
						"c/ filmEnthusiast", IllegalArgumentException.class } };

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

			this.editFilmEnthusiast(username, name, surname, photo, email,
					phoneNumber, address);

			unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void editFilmEnthusiast(String username, String name,
			String surname, String photo, String email, String phoneNumber,
			String address) {

		EditionFormObject filmEnthusiastForm = new EditionFormObject(
				this.filmEnthusiastService.findByUsername(username));
		FilmEnthusiast newAdmin = new FilmEnthusiast();
		BindingResult binding = null;

		filmEnthusiastForm.setUsername(username);
		filmEnthusiastForm.setName(name);
		filmEnthusiastForm.setSurname(surname);
		filmEnthusiastForm.setPhoto(photo);
		filmEnthusiastForm.setEmail(email);
		filmEnthusiastForm.setPhoneNumber(phoneNumber);
		filmEnthusiastForm.setAddress(address);

		newAdmin = this.reconstructEditionTest(filmEnthusiastForm, binding);

		this.filmEnthusiastService.save(newAdmin);
	}

	public FilmEnthusiast reconstructEditionTest(final EditionFormObject form,
			final BindingResult binding) {

		Actor principal = this.actorService.findByPrincipal();

		/* Creating filmEnthusiast */
		final FilmEnthusiast res = this.filmEnthusiastService.create();

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
				this.actorService.checkEmail(form.getEmail(), "FILMENTHUSIAST"),
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