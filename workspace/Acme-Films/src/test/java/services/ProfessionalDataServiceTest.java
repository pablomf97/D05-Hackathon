
package services;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Critic;
import domain.Curricula;
import domain.ProfessionalData;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProfessionalDataServiceTest extends AbstractTest {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ProfesionalDataService	professionalDataService;


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
	 * Manage ProfessionalData, which includes listing, displaying,
	 * creating, updating, and deleting its records.
	 */

	/*
	 * ####################### TEST CREATE PROFESSIONALDATA #######################
	 */

	@Test
	public void driverCreateProfessionalData() {
		final Object testingData[][] = {

			{
				"critic1", "title1", "description1", new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), null
			//Positive

			}, {
				"critic1", null, "description1", new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), IllegalArgumentException.class
			//Negative: degree dont be null

			}, {
				null, "title1", "description1", new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), IllegalArgumentException.class
			//Negative: actor is null

			}, {
				"moderator1", "title1", "description1", new GregorianCalendar(2041, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), ClassCastException.class
			//Negative: actor non authorized

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateProfessionalData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (Date) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateCreateProfessionalData(final String username, final String title, final String description, final Date startDate, final Date endDate, final Class<?> expected) {
		Critic principal;
		Class<?> caught = null;

		try {
			this.authenticate(username);

			principal = (Critic) this.actorService.findByPrincipal();

			final Curricula principalCurriculas = principal.getCurricula();
			final ProfessionalData ed = this.professionalDataService.create();

			ed.setTitle(title);
			ed.setDescription(description);
			ed.setStartDate(startDate);
			ed.setEndDate(endDate);

			this.professionalDataService.save(ed, principalCurriculas.getId());

			this.professionalDataService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST EDIT PROFESSIONALDATA #######################
	 */

	@Test
	public void driverEditProfessionalData() {
		final Object testingData[][] = {

			/* Test 1.1 ----------------------------------------------- */
			{
				"critic1", "title1", "description1", new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), null
			//Positive

			},

			/* Test 1.3 ----------------------------------------------- */
			{
				"critic1", "title1", "description1", new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), null
			//Positive

			}, {
				"moderator1", "title1", "description1", new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), ClassCastException.class
			//Negative:actor non authorized

			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditProfessionalData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (Date) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateEditProfessionalData(final String username, final String title, final String description, final Date startDate, final Date endDate, final Class<?> expected) {
		Critic principal;
		Class<?> caught = null;

		try {
			this.authenticate(username);
			principal = (Critic) this.actorService.findByPrincipal();

			final Curricula principalCurriculas = principal.getCurricula();
			final ProfessionalData ed = principalCurriculas.getProfessionalData().iterator().next();

			ed.setTitle(title);
			ed.setDescription(description);
			ed.setStartDate(startDate);
			ed.setEndDate(endDate);

			this.professionalDataService.save(ed, principalCurriculas.getId());

			this.professionalDataService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST DELETE PROFESSIONALDATA #######################
	 */

	@Test
	public void driverDeleteProfessionalData() {
		final Object testingData[][] = {

			{
				"moderator1", "professionalData1", ClassCastException.class
			//Negative: Actor non authorized

			},

			/* Test 2.3 ----------------------------------------------- */
			{
				null, "professionalData1", IllegalArgumentException.class
			//Negative: Actor null

			}, /* Test 1.1 ----------------------------------------------- */
			{
				"critic1", "professionalData1", null
			//Positive

			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteProfessionalData((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteProfessionalData(final String username, final int edId, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			final ProfessionalData ed = this.professionalDataService.findOne(edId);

			this.professionalDataService.delete(ed);

			this.professionalDataService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
