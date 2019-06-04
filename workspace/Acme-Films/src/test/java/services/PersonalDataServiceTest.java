
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Critic;
import domain.Curricula;
import domain.PersonalData;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PersonalDataServiceTest extends AbstractTest {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private PersonalDataService	personalDataService;


	//Caso de uso 17.1

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
	 * Coverage of the total project (%): TODO%
	 * 
	 * 
	 * Coverage of the total project (lines of codes): TODO
	 */

	/*
	 * 
	 * During this test we are going to try:
	 * 
	 * Manage PersonalData, which includes listing, displaying,
	 * creating, updating, and deleting its records.
	 */

	/*
	 * ####################### TEST CREATE PersonalData#######################
	 */

	@Test
	public void driverCreatePersonalData() {
		final Object testingData[][] = {

			/* Test 1.1 ----------------------------------------------- */
			{
				"critic1", "https://www.linkedin.com/in/fswe12fd", "Carlos Plaza", "statement1", "618922568", null
			//Positive

			},

			/* Test 1.2 ----------------------------------------------- */
			{
				"critic1", null, "Carlos Plaza", "statement1", "618922568", IllegalArgumentException.class
			//Negative: attribute linkedin is null

			},

			/* Test 1.3 ----------------------------------------------- */
			{
				"critic1", "url", "Carlos Plaza", "statement1", "618922568", ConstraintViolationException.class
			//Negative: format error url

			},

			/* Test 2.1 ----------------------------------------------- */
			{
				null, "https://www.linkedin.com/in/fswe12fd", "Carlos Plaza", "statement1", "618922568", IllegalArgumentException.class
			//Negative: actor null

			},

			/* Test 2.2 ----------------------------------------------- */
			{
				"moderator1", "https://www.linkedin.com/in/fswe12fd", "Carlos Plaza", "statement1", "618922568", ClassCastException.class
			//Negative: actor non authorized

			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreatePersonalData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateCreatePersonalData(final String username, final String linkedIn, final String fullName, final String statement, final String phoneNumber, final Class<?> expected) {
		Critic principal;
		Class<?> caught = null;

		try {
			this.authenticate(username);

			principal = (Critic) this.actorService.findByPrincipal();

			final Curricula principalCurriculas = principal.getCurricula();

			final PersonalData pd = this.personalDataService.create();

			pd.setLinkedIn(linkedIn);
			pd.setFullName(fullName);
			pd.setStatement(statement);
			pd.setPhoneNumber(phoneNumber);

			this.personalDataService.save(pd, principalCurriculas.getId());

			this.personalDataService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	/*
	 * ####################### TEST EDIT PersonalData#######################
	 */

	@Test
	public void driverEditPersonalData() {
		final Object testingData[][] = {

			/* Test 1.1 ----------------------------------------------- */
			{
				"critic2", "https://www.linkedin.com/in/fswe12fd", "Jesús Plaza", "statement1", "618922568", null
			//Positive
			},

			/* Test 1.3 ----------------------------------------------- */
			{
				"critic2", "url", "Jesús Plaza", "statement1", "618922568", ConstraintViolationException.class
			//Negative: formatt error url

			}, {
				"moderator1", "https://www.linkedin.com/in/fswe12fd", "Jesús Plaza", "statement1", "618922568", ClassCastException.class
			// Negative: actor non authorized

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditPersonalData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateEditPersonalData(final String username, final String linkedIn, final String fullName, final String statement, final String phoneNumber, final Class<?> expected) {
		Critic principal;
		Class<?> caught = null;

		try {
			this.authenticate(username);
			principal = (Critic) this.actorService.findByPrincipal();

			final Curricula principalCurriculas = principal.getCurricula();
			final PersonalData pd = principalCurriculas.getPersonalData();

			pd.setLinkedIn(linkedIn);
			pd.setFullName(fullName);
			pd.setStatement(statement);
			pd.setPhoneNumber(phoneNumber);

			this.personalDataService.save(pd, principalCurriculas.getId());

			this.personalDataService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
