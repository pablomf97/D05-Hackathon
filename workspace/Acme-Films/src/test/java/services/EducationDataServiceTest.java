
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
import domain.EducationData;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EducationDataServiceTest extends AbstractTest {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private EducationDataService	educationDataService;


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
	 * Manage EducationData, which includes listing, displaying,
	 * creating, updating, and deleting its records.
	 */

	/*
	 * ####################### TEST CREATE EDUCATIONDATA #######################
	 */

	@Test
	public void driverCreateEducationData() {
		final Object testingData[][] = {

			{
				"critic1", "degree1", "institucion1", 9.5, new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), null
			//Positive

			}, {
				"critic1", null, "institucion1", 9.5, new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), IllegalArgumentException.class
			//Negative: degree dont be null

			}, {
				null, "degree1", "institucion1", 9.5, new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), IllegalArgumentException.class
			//Negative: actor is null

			}, {
				"moderator1", "degree1", "institucion1", 9.5, new GregorianCalendar(2041, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), ClassCastException.class
			//Negative: actor non authorized

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateEducationData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Double) testingData[i][3], (Date) testingData[i][4], (Date) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateCreateEducationData(final String username, final String degree, final String institution, final Double mark, final Date startDate, final Date endDate, final Class<?> expected) {
		Critic principal;
		Class<?> caught = null;

		try {
			this.authenticate(username);

			principal = (Critic) this.actorService.findByPrincipal();

			final Curricula principalCurriculas = principal.getCurricula();

			final EducationData ed = this.educationDataService.create();

			ed.setDegree(degree);
			ed.setInstitution(institution);
			ed.setMark(mark);
			ed.setStartDate(startDate);
			ed.setEndDate(endDate);

			this.educationDataService.save(ed, principalCurriculas.getId());

			this.educationDataService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST EDIT EDUCATIONDATA #######################
	 */

	@Test
	public void driverEditEducationData() {
		final Object testingData[][] = {

			/* Test 1.1 ----------------------------------------------- */
			{
				"critic1", "degree1", "institucion1", 9.5, new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), null
			//Positive

			},

			/* Test 1.3 ----------------------------------------------- */
			{
				"critic1", "degree1", "institucion1", null, new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), null
			//Positive

			}, {
				"moderator1", "degree1", "institucion1", null, new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2011, Calendar.JANUARY, 1).getTime(), ClassCastException.class
			//Negative:actor non authorized

			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditEducationData((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Double) testingData[i][3], (Date) testingData[i][4], (Date) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateEditEducationData(final String username, final String degree, final String institution, final Double mark, final Date startDate, final Date endDate, final Class<?> expected) {
		Critic principal;
		Class<?> caught = null;

		try {
			this.authenticate(username);
			principal = (Critic) this.actorService.findByPrincipal();

			final Curricula principalCurriculas = principal.getCurricula();
			final EducationData ed = principalCurriculas.getEducationData().iterator().next();

			ed.setDegree(degree);
			ed.setInstitution(institution);
			ed.setMark(mark);
			ed.setStartDate(startDate);
			ed.setEndDate(endDate);

			this.educationDataService.save(ed, principalCurriculas.getId());

			this.educationDataService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST DELETE EDUCAITONDATA #######################
	 */

	@Test
	public void driverDeleteEducationData() {
		final Object testingData[][] = {

			{
				"moderator1", "educationData1", ClassCastException.class
			//Negative: Actor non authorized

			},

			/* Test 2.3 ----------------------------------------------- */
			{
				null, "educationData1", IllegalArgumentException.class
			//Negative: Actor null

			}, /* Test 1.1 ----------------------------------------------- */
			{
				"critic1", "educationData1", null
			//Positive

			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteEducationData((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteEducationData(final String username, final int edId, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			final EducationData ed = this.educationDataService.findOne(edId);

			this.educationDataService.delete(ed);

			this.educationDataService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
