
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
import domain.MiscellaneousData;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MiscellaneousDataServiceTest extends AbstractTest {

	@Autowired
	private ActorService				actorService;

	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;


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
	 * Manage MiscellaneousData, which includes listing, displaying,
	 * creating, updating, and deleting its records.
	 */

	/*
	 * ####################### TEST CREATE MISCELLANEOUSDATA #######################
	 */

	@Test
	public void driverCreateMiscellaneousData() {
		final Object testingData[][] = {

			/* Test 1.1 ----------------------------------------------- */
			{
				"critic1", "text", "https://www.imgur.com/djkskdb", null
			//Positive

			},

			/* Test 1.2 ----------------------------------------------- */
			{
				"critic1", null, "https://www.imgur.com/djkskdb", IllegalArgumentException.class
			//Negative: attribute text is null

			},

			/* Test 1.3 ----------------------------------------------- */
			{
				"critic1", "text", "url", ConstraintViolationException.class
			//Negative: format error url

			},

			/* Test 2.1 ----------------------------------------------- */
			{
				null, "title", "description", IllegalArgumentException.class
			//Negative: actor null

			},

			/* Test 2.2 ----------------------------------------------- */
			{
				"moderator1", "title", "description", ClassCastException.class
			//Negative: actor non authorized

			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateMiscellaneousData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateCreateMiscellaneousData(final String username, final String text, final String attachement, final Class<?> expected) {
		Critic principal;
		Class<?> caught = null;

		try {
			this.authenticate(username);

			principal = (Critic) this.actorService.findByPrincipal();

			final Curricula principalCurriculas = principal.getCurricula();

			final MiscellaneousData miscellaneousData = this.miscellaneousDataService.create();

			miscellaneousData.setText(text);
			miscellaneousData.setAttachements(attachement);

			this.miscellaneousDataService.save(miscellaneousData, principalCurriculas.getId());

			this.miscellaneousDataService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST EDIT MISCELLANEOUSDATA #######################
	 */

	@Test
	public void driverEditMiscellaneousData() {
		final Object testingData[][] = {

			/* Test 1.1 ----------------------------------------------- */
			{
				"critic1", "text", "https://gadsy.com/auifd", null
			//Positive

			},

			/* Test 1.3 ----------------------------------------------- */
			{
				"critic1", "miscellaneousRecord1", "url", ConstraintViolationException.class
			//Negative: formatt error url

			}, {
				"moderator1", "miscellaneousRecord1", "https://gadsy.com/auifd", ClassCastException.class
			// Negative: actor non authorized

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditMiscellaneousData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateEditMiscellaneousData(final String username, final String text, final String attachements, final Class<?> expected) {
		Critic principal;
		Class<?> caught = null;

		try {
			this.authenticate(username);
			principal = (Critic) this.actorService.findByPrincipal();

			final Curricula principalCurriculas = principal.getCurricula();
			final MiscellaneousData miscellaneousRecord = principalCurriculas.getMiscellaneousData().iterator().next();

			miscellaneousRecord.setText(text);
			miscellaneousRecord.setAttachements(attachements);

			this.miscellaneousDataService.save(miscellaneousRecord, principalCurriculas.getId());

			this.miscellaneousDataService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST DELETE MISCELLANEOUSDATA #######################
	 */

	@Test
	public void driverDeleteMiscellaneousData() {
		final Object testingData[][] = {

			{
				"moderator1", "miscellaneousData1", ClassCastException.class
			//Negative: Actor non authorized

			},

			/* Test 2.3 ----------------------------------------------- */
			{
				null, "miscellaneousData1", IllegalArgumentException.class
			//Negative: Actor null

			}, /* Test 1.1 ----------------------------------------------- */
			{
				"critic1", "miscellaneousData1", null
			//Positive

			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteMiscellaneousData((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteMiscellaneousData(final String username, final int miscId, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			final MiscellaneousData miscellaneousRecord = this.miscellaneousDataService.findOne(miscId);

			this.miscellaneousDataService.delete(miscellaneousRecord);

			this.miscellaneousDataService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
