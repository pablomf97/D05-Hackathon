package services;

import javax.transaction.Transactional;

import javax.validation.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.SocialProfile;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SocialProfileServiceTest extends AbstractTest {

	@Autowired
	private SocialProfileService socialProfileService;

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
	 * Coverage of the total project (%): 4,1
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 1.473
	 */

	/*
	 * ###################### TEST CREATE PERIOD RECORDS ######################
	 */

	@Test
	public void driverCreateSocialProfile() {
		Object testingData[][] = {

				/* 1. Correct Testing */

				{ "moderator1", "https://www.facebook.com/", "Guts",
						"Facebook", null },

				/* Incorrect Testing */

				{ "admin", "https://www.youtube.com/", "Serpico", "",
						ConstraintViolationException.class },

				{ "admin", "twitter", "Schierke", "Twitter",
						ConstraintViolationException.class },

				{ "", "https://www.reddit.com/", "Casca", "Reddit",
						IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSocialProfile((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void templateCreateSocialProfile(String username, String link,
			String nick, String socialNetwork, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			SocialProfile socialProfile = this.socialProfileService.create();

			socialProfile.setLink(link);
			socialProfile.setNick(nick);
			socialProfile.setSocialNetwork(socialNetwork);

			this.socialProfileService.save(socialProfile);

			this.socialProfileService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ##########################################################################
	 */

	@Test
	public void driverEditLegalRecord() {
		Object testingData[][] = {

				{ "admin", 199, "https://www.instagram.com/", "Zodd",
						"Instagram", null },

				/* Incorrect Testing */

				{ "admin", 199, "https://www.linkedin.com/", "Griffith", "",
						ConstraintViolationException.class },

				{ "admin", 199, "vk", "Judeau", "vk",
						ConstraintViolationException.class },

				{ "", 199, "https://www.pinterest.com/", "Pipin", "Pinterest",
						IllegalArgumentException.class },

				{ "moderator", 199, "https://www.flickr.com/", "Puck",
						"Flickr", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateEditSocialProfile((String) testingData[i][0],
					(int) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4],
					(Class<?>) testingData[i][5]);
	}

	protected void templateEditSocialProfile(String username, int socialid,
			String link, String nick, String socialNetwork, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			SocialProfile socialProfile = this.socialProfileService
					.findOne(socialid);

			socialProfile.setLink(link);
			socialProfile.setNick(nick);
			socialProfile.setSocialNetwork(socialNetwork);

			this.socialProfileService.save(socialProfile);

			this.socialProfileService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ##########################################################################
	 */

	@Test
	public void driverDeleteLegalRecord() {
		Object testingData[][] = {

		{ "admin", 199, null },

		/* Incorrect Testing */

		{ "moderator1", 199, IllegalArgumentException.class },

		{ "", 199, IllegalArgumentException.class }

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteSocialProfile((String) testingData[i][0],
					(int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteSocialProfile(String username, int socialid,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			SocialProfile socialProfile = this.socialProfileService
					.findOne(socialid);

			this.socialProfileService.delete(socialProfile);

			this.socialProfileService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
