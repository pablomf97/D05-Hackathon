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

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Critic;
import forms.RegisterFormObject;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CriticServiceTest extends AbstractTest {

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
	private CriticService criticService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	// Tests ----------------------------------------------------

	// Test: Caso de uso:
	// An actor who is not authenticated must be able to:
	// Register to the system as a critic. (7.1)
	@Test
	public void driverRegister() {
		Object registerTestingData[][] = {
				/* Positive case */
				{ "criticT", "criticT", "criticT", "criticT",
						"https://www.foto.com", "criticT@criticT.criticT",
						"666666666", "c/ criticT", null },
				/* Negative: invalid critic */
				{ "", "criticT", "criticT", "criticT", "https://www.foto.com",
						"criticT@criticT.criticT", "666666666", "c/ criticT",
						IllegalArgumentException.class },
				/* Negative cases: invalid data */
				{ "criticT2", "", "criticT", "criticT", "https://www.foto.com",
						"criticT@", "666666666", "c/ criticT",
						IllegalArgumentException.class },
				{ "criticT3", "criticT", "", "", "https://www.foto.com",
						"criticT@", "666666666", "c/ criticT",
						IllegalArgumentException.class },
				{ "criticT4", "criticT", "criticT", "criticT",
						"https://www.foto.com", null, "666666666",
						"c/ criticT", IllegalArgumentException.class },
				{ "criticT5", "criticT", "criticT", "criticT",
						"https://www.foto.com", "criticT@", "666666666",
						"c/ criticT", IllegalArgumentException.class } };

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
			this.registerCritic(username, password, name, surname, photo,
					email, phoneNumber, address);

			unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void registerCritic(String username, String password, String name,
			String surname, String photo, String email, String phoneNumber,
			String address) {

		RegisterFormObject criticForm = new RegisterFormObject();
		Critic newAdmin = new Critic();

		criticForm.setUsername(username);
		criticForm.setPassword(password);
		criticForm.setPassConfirmation(password);
		criticForm.setTermsAndConditions(true);
		criticForm.setName(name);
		criticForm.setSurname(surname);
		criticForm.setPhoto(photo);
		criticForm.setEmail(email);
		criticForm.setPhoneNumber(phoneNumber);
		criticForm.setAddress(address);

		newAdmin = this.reconstructNoBinding(criticForm);

		this.criticService.save(newAdmin);
	}

	private Critic reconstructNoBinding(RegisterFormObject form) {
		final Critic res = this.criticService.create();

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
		Assert.isTrue(form.getPassword().equals(form.getPassConfirmation()),
				"pass.confirm.error");
		Assert.isTrue(form.getPassword() != null
				&& !form.getPassword().isEmpty());

		/* Terms&Conditions */
		Assert.isTrue((form.getTermsAndConditions()), "terms.error");

		Assert.isTrue(form.getEmail() != null && !form.getEmail().isEmpty());
		Assert.isTrue(this.actorService.checkEmail(form.getEmail(), "CRITIC"),
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

	// @Test
	// public void driverEdit() {
	// Object editionTestingData[][] = {
	// /* Positive case */
	// { "critic", "critic", "critic",
	// "https://www.foto.com",
	// "critic@",
	// "666666666", "c/ critic", null },
	// /* Negative cases: invalid data */
	// { "", "critic", "critic", "https://www.foto.com",
	// "critic@critic.critic", "666666666",
	// "c/ critic",
	// IllegalArgumentException.class },
	// { "critic", "critic", "critic",
	// "https://www.foto.com",
	// "critic@critic.critic", "666666666",
	// "c/ critic",
	// IllegalArgumentException.class },
	// { "critic", "", "", "https://www.foto.com", "critic@",
	// "666666666", "c/ critic", IllegalArgumentException.class },
	// { "critic", "critic", "critic",
	// "https://www.foto.com", "",
	// "666666666", "c/ critic", IllegalArgumentException.class } };
	//
	// for (int i = 0; i < editionTestingData.length; i++) {
	// templateEdit((String) editionTestingData[i][0],
	// (String) editionTestingData[i][1],
	// (String) editionTestingData[i][2],
	// (String) editionTestingData[i][3],
	// (String) editionTestingData[i][4],
	// (String) editionTestingData[i][5],
	// (String) editionTestingData[i][6],
	// (Class<?>) editionTestingData[i][7]);
	// }
	// }
	//
	// protected void templateEdit(String username, String name, String surname,
	// String photo, String email, String phoneNumber, String address,
	// Class<?> expected) {
	// Class<?> caught;
	//
	// caught = null;
	//
	// try {
	// authenticate(username);
	//
	// this.editCritic(username, name, surname, photo, email,
	// phoneNumber, address);
	//
	// unauthenticate();
	// } catch (Throwable oops) {
	// caught = oops.getClass();
	// }
	//
	// super.checkExceptions(expected, caught);
	// }
	//
	// public void editCritic(String username, String name, String
	// surname,
	// String photo, String email, String phoneNumber, String address) {
	//
	// EditionFormObject criticForm = new EditionFormObject(
	// this.criticService.findByUsername(username));
	// Critic newAdmin = new Critic();
	// BindingResult binding = null;
	//
	// criticForm.setUsername(username);
	// criticForm.setName(name);
	// criticForm.setSurname(surname);
	// criticForm.setPhoto(photo);
	// criticForm.setEmail(email);
	// criticForm.setPhoneNumber(phoneNumber);
	// criticForm.setAddress(address);
	//
	// newAdmin = this.reconstructEditionTest(criticForm, binding);
	//
	// this.criticService.save(newAdmin);
	// }
	//
	// public Critic reconstructEditionTest(final EditionFormObject
	// form,
	// final BindingResult binding) {
	//
	// Actor principal = this.actorService.findByPrincipal();
	//
	// /* Creating critic */
	// final Critic res = this.criticService.create();
	//
	// res.setId(form.getId());
	// res.setVersion(form.getVersion());
	// res.setName(form.getName());
	// res.setSurname(form.getSurname());
	// res.setPhoto(form.getPhoto());
	// res.setEmail(form.getEmail());
	// res.setPhoneNumber(form.getPhoneNumber());
	// res.setAddress(form.getAddress());
	// res.setIsSpammer(principal.getIsSpammer());
	// res.setSocialProfile(principal.getSocialProfile());
	// res.setMessageBoxes(principal.getMessageBoxes());
	//
	// Assert.isTrue(form.getEmail() != null && !form.getEmail().isEmpty());
	// Assert.isTrue(this.actorService.checkEmail(form.getEmail(),
	// "CRITIC"),
	// "actor.email.error");
	//
	// Assert.isTrue(form.getSurname() != null && !form.getSurname().isEmpty());
	// Assert.isTrue(form.getName() != null && !form.getName().isEmpty());
	//
	// /* Managing phone number */
	// if (form.getPhoneNumber() != null) {
	// try {
	// final char[] phoneArray = form.getPhoneNumber().toCharArray();
	// if ((!form.getPhoneNumber().equals(null) && !form
	// .getPhoneNumber().equals("")))
	// if (phoneArray[0] != '+'
	// && Character.isDigit(phoneArray[0])) {
	// final String cc = this.systemConfigurationService
	// .findMySystemConfiguration().getCountryCode();
	// form.setPhoneNumber(cc + " " + form.getPhoneNumber());
	// }
	// } catch (Throwable oops) {
	// binding.rejectValue("phoneNumber", "phone.error");
	// }
	// }
	//
	// return res;
	// }

}