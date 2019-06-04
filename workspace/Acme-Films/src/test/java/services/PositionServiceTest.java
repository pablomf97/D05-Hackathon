package services;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Position;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PositionServiceTest extends AbstractTest {

	@Autowired
	private PositionService positionService;

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
	public void driverCreatePosition() {
		Object testingData[][] = {

				/* 1. Correct Testing */

				{ "moderator1", "English" , "Position 1", "Español", "Posición 1", null },

				/* Incorrect Testing */

				{ "moderator1", "" , "Position 1", "Español", "Posición 1", NullPointerException.class },

				{ "", "English" , "Position 1", "Español", "Posición 1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreatePosition((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateCreatePosition(String username, String eng, String nameEng, String spa, String nameSpa, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Position position = this.positionService.create();
			Map<String,String> mapName = new HashMap<>();
			
			mapName.put(eng, nameEng);
			mapName.put(spa, nameSpa);
			
			position.setName(mapName);

			this.positionService.save(position);

			this.positionService.flush();

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
	public void driverEditPosition() {
		Object testingData[][] = {
				
				/* 1. Correct Testing */

				{ "moderator1", "position1", "English" , "Position 1", "Español", "Posición 1", null },

				/* Incorrect Testing */
				
				{ "moderator1", "position1", "English" , "Position 1", "", "Posición 1", NullPointerException.class },

				{ "", "position1", "English" , "Position 1", "Español", "Posición 1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateEditPosition((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateEditPosition(String username, String positionName, String eng,
			String nameEng, String spa, String nameSpa, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Position position = this.positionService
					.findOne(this.getEntityId(positionName));

			Map<String,String> mapName = new HashMap<>();
			
			mapName.put(eng, nameEng);
			mapName.put(spa, nameSpa);
			
			position.setName(mapName);

			this.positionService.save(position);

			this.positionService.flush();

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
	public void driverDeletePosition() {
		Object testingData[][] = {

		{ "moderator1", "position5", null },

		/* Incorrect Testing */

		{ "moderator", "position1", IllegalArgumentException.class },
		
		{ "", "position1", IllegalArgumentException.class },

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeletePosition((String) testingData[i][0],
					(String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeletePosition(String username, String positionName,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Position position = this.positionService
					.findOne(this.getEntityId(positionName));

			this.positionService.delete(position);

			this.positionService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
