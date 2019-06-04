package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Saga;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SagaServiceTest extends AbstractTest {

	@Autowired
	private SagaService sagaService;

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

	/*
	 * ###################### TEST CREATE PERIOD RECORDS ######################
	 */

	@Test
	public void driverCreateSaga() {
		Object testingData[][] = {

				/* 1. Correct Testing */

				{ "moderator1", "Saga 1", null },

				/* Incorrect Testing */

				{ "admin", "Saga 1", IllegalArgumentException.class },

				{ "", "Saga 1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateSaga((String) testingData[i][0],
					(String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateCreateSaga(String username, String title, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Saga saga = this.sagaService.create();

			saga.setTitle(title);

			this.sagaService.save(saga);

			this.sagaService.flush();

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
	public void driverEditSaga() {
		Object testingData[][] = {
				
				/* 1. Correct Testing */

				{ "moderator1", "saga6", "Saga 1", null },

				/* Incorrect Testing */

				{ "admin", "saga6", "Saga 1", IllegalArgumentException.class },

				{ "", "saga6", "Saga 1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateEditSaga((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	protected void templateEditSaga(String username, String sagaString,
			String title, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Saga saga = this.sagaService
					.findOne(this.getEntityId(sagaString));

			saga.setTitle(title);

			this.sagaService.save(saga);

			this.sagaService.flush();

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
	public void driverDeleteSaga() {
		Object testingData[][] = {

		{ "moderator1", "saga6", null },

		/* Incorrect Testing */

		{ "critic1", "saga6", IllegalArgumentException.class },

		{ "", "saga6", IllegalArgumentException.class },

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteSaga((String) testingData[i][0],
					(String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteSaga(String username, String sagaName,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Saga saga = this.sagaService
					.findOne(this.getEntityId(sagaName));

			this.sagaService.delete(saga);

			this.sagaService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
