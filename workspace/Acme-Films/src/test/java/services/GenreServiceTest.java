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
import domain.Genre;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class GenreServiceTest extends AbstractTest {

	@Autowired
	private GenreService genreService;

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
	public void driverCreateGenre() {
		Object testingData[][] = {

				/* 1. Correct Testing */

				{ "moderator1", "English" , "Genre 1", "Español", "Género 1", null },

				/* Incorrect Testing */

				{ "moderator1", "" , "Genre 1", "Español", "Género 1", NullPointerException.class },

				{ "", "English" , "Genre 1", "Español", "Género 1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateGenre((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateCreateGenre(String username, String eng, String nameEng, String spa, String nameSpa, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Genre genre = this.genreService.create();
			Map<String,String> mapName = new HashMap<>();
			
			mapName.put(eng, nameEng);
			mapName.put(spa, nameSpa);
			
			genre.setName(mapName);

			this.genreService.save(genre);

			this.genreService.flush();

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
	public void driverEditGenre() {
		Object testingData[][] = {
				
				/* 1. Correct Testing */

				{ "moderator1", "genre1", "English" , "Genre 1", "Español", "Género 1", null },

				/* Incorrect Testing */
				
				{ "moderator1", "genre1", "English" , "Genre 1", "", "Género 1", NullPointerException.class },

				{ "", "genre1", "English" , "Genre 1", "Español", "Género 1", IllegalArgumentException.class } };

		for (int i = 0; i < testingData.length; i++)
			this.templateEditGenre((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateEditGenre(String username, String genreName, String eng,
			String nameEng, String spa, String nameSpa, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Genre genre = this.genreService
					.findOne(this.getEntityId(genreName));

			Map<String,String> mapName = new HashMap<>();
			
			mapName.put(eng, nameEng);
			mapName.put(spa, nameSpa);
			
			genre.setName(mapName);

			this.genreService.save(genre);

			this.genreService.flush();

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
	public void driverDeleteGenre() {
		Object testingData[][] = {

		{ "moderator1", "genre5", null },

		/* Incorrect Testing */

		{ "moderator", "genre1", IllegalArgumentException.class },
		
		{ "", "genre1", IllegalArgumentException.class },

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteGenre((String) testingData[i][0],
					(String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteGenre(String username, String genreName,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Genre genre = this.genreService
					.findOne(this.getEntityId(genreName));

			this.genreService.delete(genre);

			this.genreService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
