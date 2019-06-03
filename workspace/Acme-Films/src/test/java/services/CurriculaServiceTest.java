
package services;

import javax.transaction.Transactional;

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
public class CurriculaServiceTest extends AbstractTest {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private CurriculaService	curriculaService;

	@Autowired
	private PersonalDataService	personalDataService;

	@Autowired
	private CriticService		criticService;


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
	 * Coverage of the total project (%):TODO%
	 * 
	 * 
	 * Coverage of the total project (lines of codes): TODO
	 */

	//TEST CREATE CURRICULA //

	@Test
	public void driverCreateCV() {
		final Object testingData[][] = {

			/* Test 1.1 ----------------------------------------------- */
			{
				"critic3", null
			//Positive

			}, {
				"moderator1", ClassCastException.class
			//Negative:Actor non authorized

			}, {
				"admin", ClassCastException.class
			//Negative:Actor non authorized

			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreatecv((String) testingData[i][0],

			(Class<?>) testingData[i][1]);
	}

	protected void templateCreatecv(final String username, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Curricula cv = this.curriculaService.create();

			final PersonalData pd = this.personalDataService.create();
			pd.setFullName("Test curricula");
			pd.setLinkedIn("https://www.linkedin.com/in/fswe12fd");
			pd.setStatement("Test curricula");
			this.personalDataService.saveNewCurricula(pd);

			final Curricula c = this.curriculaService.saveNewCurricula(pd);
			this.criticService.newCurricula(c);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	///TEST DELETE CURRICULA///
	@Test
	public void driverDeleteCV() {
		final Object testingData[][] = {

			/* Test 1.1 ----------------------------------------------- */
			{
				"Critic1", null
			//Positive

			}, {
				"Moderator1", ClassCastException.class
			//Negative:Actor non authorized

			}, {
				"Admin", ClassCastException.class
			//Negative:Actor non authorized

			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeletecv((String) testingData[i][0],

			(Class<?>) testingData[i][1]);
	}

	protected void templateDeletecv(final String username, final Class<?> expected) {
		Critic principal;
		Class<?> caught = null;

		try {
			this.authenticate(username);

			principal = (Critic) this.actorService.findByPrincipal();

			final Curricula principalCurriculas = principal.getCurricula();

			this.curriculaService.deleteCV(principalCurriculas);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
