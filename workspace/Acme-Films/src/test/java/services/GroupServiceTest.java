
package services;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Actor;
import domain.Film;
import domain.FilmEnthusiast;
import domain.Forum;
import domain.Saga;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class GroupServiceTest extends AbstractTest {

	@Autowired
	private GroupService	groupService;

	@Autowired
	private FilmService		filmService;

	@Autowired
	private SagaService		sagaService;

	@Autowired
	private ActorService	actorService;


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

	/*
	 * 
	 * During this test we are going to try:
	 * 
	 * Manage forum, which includes listing, displaying,
	 * creating, updating, and deleting it. UC 3.1,3.2
	 */

	/*
	 * ####################### TEST CREATE FORUM By FILM #######################
	 */
	@Test
	public void driverCreateForumByFilm() throws InterruptedException {
		TimeUnit.SECONDS.sleep(2);

		final Object testingData[][] = {
			{
				"filmEnthusiast1", "film1", null, "name", "description", new Date(System.currentTimeMillis() - 1), "rejectReason", false, null
			},
			//Positive test case 

			{
				"moderator1", "film1", null, "name", "description", new Date(System.currentTimeMillis() - 1), "rejectReason", false, IllegalArgumentException.class

			},

			//Negative test case, trying to create and forum as a moderator (wrong actor).

			{
				"filmEnthusiast", "film1", "saga1", "name", "description", new Date(System.currentTimeMillis() - 1), "rejectReason", false, IllegalArgumentException.class
			},

		//Negative test case,  trying to create and forum with saga and film
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateForumFilm((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Date) testingData[i][5], (String) testingData[i][6],
				(boolean) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	protected void templateCreateForumFilm(final String username, final String film, final String saga, final String name, final String description, final Date forumDate, final String rejectReason, final boolean active, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Film movie = this.filmService.findOne(this.getEntityId(film));

			final Forum forum = this.groupService.createForFilm(movie);

			forum.setDescription(description);
			forum.setName(name);

			this.groupService.save(forum);
			this.groupService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST CREATE FORUM By SAGA #######################
	 */
	@Test
	public void driverCreateForumBySaga() throws InterruptedException {
		TimeUnit.SECONDS.sleep(4);
		final Object testingData[][] = {
			{
				"filmEnthusiast1", null, "saga1", "name", "description", new Date(System.currentTimeMillis() - 1), "rejectReason", false, null
			},
			//Positive test case 

			{
				"moderator1", null, "saga1", "name", "description", new Date(System.currentTimeMillis() - 1), "rejectReason", false, IllegalArgumentException.class

			},

			//Negative test case, trying to create and forum as a moderator (wrong actor).

			{
				"filmEnthusiast", "film1", "saga1", "name", "description", new Date(System.currentTimeMillis() - 1), "rejectReason", false, IllegalArgumentException.class
			},

		//Negative test case,  trying to create and forum with saga and film
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateForumSaga((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Date) testingData[i][5], (String) testingData[i][6],
				(boolean) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	protected void templateCreateForumSaga(final String username, final String film, final String saga, final String name, final String description, final Date forumDate, final String rejectReason, final boolean active, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Saga serie = this.sagaService.findOne(this.getEntityId(saga));

			final Forum forum = this.groupService.createForSaga(serie);

			forum.setDescription(description);
			forum.setName(name);

			this.groupService.save(forum);
			this.groupService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST edit FORUM #######################
	 */
	@Test
	public void driverEditForum() {
		final Object testingData[][] = {
			{
				"filmEnthusiast1", "forum1", "name", "description", new Date(System.currentTimeMillis() - 1), "rejectReason", false, null
			},
			//Positive test case 

			{
				"moderator1", "forum1", "name", "description", new Date(System.currentTimeMillis() - 1), "rejectReason", false, IllegalArgumentException.class

			},

			//Negative test case, trying to edit and forum as a moderator (wrong actor).

			{
				"filmEnthusiast", "forum3", "name", "description", new Date(System.currentTimeMillis() - 1), "rejectReason", false, IllegalArgumentException.class
			},

		//Negative test case,  trying to edit a forum of other person
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditForum((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Date) testingData[i][4], (String) testingData[i][5], (boolean) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	protected void templateEditForum(final String username, final String forum, final String name, final String description, final Date forumDate, final String rejectReason, final boolean active, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Forum grupo = this.groupService.findOne(this.getEntityId(forum));

			grupo.setDescription(description);
			grupo.setName(name);

			this.groupService.save(grupo);
			this.groupService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST DELETE FORUM #######################
	 */

	@Test
	public void driverDeleteForum() {
		final Object testingData[][] = {
			{
				"filmEnthusiast3", "forum5", null
			},
			//Positive test case
			{
				"filmEnthusiast1", "forum1", IllegalArgumentException.class
			},
			//Negative test case, delete in active mode

			{
				"filmEnthusiast1", "forum2", IllegalArgumentException.class
			},
			//Negative test case, filmEnthusiast1 is not owner of the group

			{
				"filmEnthusiast1", "forum200", IllegalArgumentException.class
			},
			//Negative test case, forum4 doesn't exist

			{
				"moderator1", "forum5", IllegalArgumentException.class
			},
			//Negative test case, wrong actor

			{
				null, "forum1", IllegalArgumentException.class
			}
		//Negative test case, null actor

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteForum((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateDeleteForum(final String username, final String forum, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Forum aux = this.groupService.findOne(this.getEntityId(forum));

			this.groupService.delete(aux);

			this.groupService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST list FORUM #######################
	 */

	@Test
	public void driverListForumWithoutModerator() {
		final Object testingData[][] = {

			{
				"moderator1", null
			},

			//Positive test case

			{
				"filmEnthusiast3", IllegalArgumentException.class
			},

			//Negative test case, filmEnthusiast3 doesn't correspond

			{
				null, IllegalArgumentException.class
			}

		//Negative test case, null filmEnthusiast
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListForumWithoutModerator((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateListForumWithoutModerator(final String username, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Collection<Forum> aux = this.groupService.findAllWithoutModerator();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverListForumfindByModerator() {
		final Object testingData[][] = {

			{
				"moderator1", null
			},

			//Positive test case

			{
				"filmEnthusiast3", IllegalArgumentException.class
			},

			//Negative test case, filmEnthusiast doesn't correspond

			{
				null, IllegalArgumentException.class
			}

		//Negative test case, null filmEnthusiast
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListForumfindByModerator((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateListForumfindByModerator(final String username, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Collection<Forum> aux = this.groupService.findByModerator();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverListForumfilmEnthusiast() {
		final Object testingData[][] = {

			{
				"filmEnthusiast1", null
			},

			//Positive test case

			{
				"moderator1", ClassCastException.class
			},

			//Negative test case, moderator doesn't correspond

			{
				null, IllegalArgumentException.class
			}

		//Negative test case, null filmEnthusiast
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListForumfindfilmEnthusiast((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateListForumfindfilmEnthusiast(final String username, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			final FilmEnthusiast act = (FilmEnthusiast) this.actorService.findByPrincipal();

			final Collection<Forum> aux = this.groupService.findAllByFilmEnthusiast(act.getId());

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverListForumfindAllByFilm() {
		final Object testingData[][] = {

			{
				"film1", null
			},

			//Positive test case

			{
				"saga1", IllegalArgumentException.class
			},

			//Negative test case, saga1 doesn't correspond

			{
				null, IllegalArgumentException.class
			}

		//Negative test case, null film
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListForumfindAllByFilm((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateListForumfindAllByFilm(final String film, final Class<?> expected) {

		Class<?> caught = null;

		try {

			final Collection<Forum> aux = this.groupService.findAllByFilm((this.getEntityId(film)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverListForumfindAllBySaga() {
		final Object testingData[][] = {

			{
				"saga1", null
			},

			//Positive test case

			{
				"film1", IllegalArgumentException.class
			},

			//Negative test case, film1 doesn't correspond

			{
				null, IllegalArgumentException.class
			}

		//Negative test case, null saga
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListForumfindAllBySaga((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateListForumfindAllBySaga(final String saga, final Class<?> expected) {

		Class<?> caught = null;

		try {

			final Collection<Forum> aux = this.groupService.findAllBySaga((this.getEntityId(saga)));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST request FORUM #######################
	 */

	@Test
	public void driverRequestForum() {
		final Object testingData[][] = {
			{
				"filmEnthusiast3", "forum3", null
			},
			//Positive test case

			{
				"filmEnthusiast2", "forum3", IllegalArgumentException.class
			},
			//Negative test case, filmEnthusiast is creator of the forum

			{
				"filmEnthusiast2", "forum1", IllegalArgumentException.class
			},
			//Negative test case, the group of the forum yet belong to the forum

			{
				"moderator1", "forum1", IllegalArgumentException.class
			},
			//Negative test case, wrong actor

			{
				null, "forum1", IllegalArgumentException.class
			}
		//Negative test case, null actor

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRequestForum((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateRequestForum(final String username, final String forum, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Forum aux = this.groupService.findOne(this.getEntityId(forum));

			this.groupService.requestGroup(aux.getId());

			this.groupService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	/*
	 * ####################### TEST deleteMember FORUM #######################
	 */

	@Test
	public void driverdeleteMemberForum() {
		final Object testingData[][] = {
			{
				"filmEnthusiast2", "filmEnthusiast1", "forum3", null
			},
			//Positive test case

			{
				"filmEnthusiast2", "filmEnthusiast2", "forum2", IllegalArgumentException.class
			},
			//Negative test case, filmEnthusiast is creator of the forum

			{
				"filmEnthusiast2", "filmEnthusiast3", "forum3", IllegalArgumentException.class
			},
			//Negative test case, the filmEnthusiast of the forum don´t belong to the forum

			{
				"filmEnthusiast3", "filmEnthusiast1", "forum3", IllegalArgumentException.class
			}
		//Negative test case, wrong actor

		};

		for (int i = 0; i < testingData.length; i++)
			this.templatedeleteMemberForum((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	protected void templatedeleteMemberForum(final String username, final String usernameD, final String forum, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(usernameD);
			final Actor act = this.actorService.findByPrincipal();
			this.unauthenticate();
			this.authenticate(username);

			final Forum aux = this.groupService.findOne(this.getEntityId(forum));
			this.groupService.deleteMember(act.getId(), aux.getId());

			this.groupService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST active FORUM #######################
	 */

	@Test
	public void driveractiveForum() {
		final Object testingData[][] = {

			{
				"filmEnthusiast2", "forum5", IllegalArgumentException.class
			},
			//Negative test case, filmEnthusiast is creator of the forum

			{
				null, "forum5", IllegalArgumentException.class
			},
			//Negative test case, the filmEnthusiast of the forum don´t belong to the forum

			{
				"moderator1", "forum5", null
			}
		//Positive test case

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateactiveForum((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateactiveForum(final String username, final String forum, final Class<?> expected) {

		Class<?> caught = null;

		try {

			this.authenticate(username);

			final Forum aux = this.groupService.findOne(this.getEntityId(forum));
			this.groupService.activateGroup(aux);

			this.groupService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	/*
	 * ####################### TEST deactive FORUM #######################
	 */

	@Test
	public void driverdeactiveForum() {
		final Object testingData[][] = {

			{
				"filmEnthusiast2", "forum5", IllegalArgumentException.class
			},
			//Negative test case, filmEnthusiast is creator of the forum

			{
				null, "forum5", IllegalArgumentException.class
			},
			//Negative test case, the filmEnthusiast of the forum don´t belong to the forum

			{
				"moderator1", "forum5", null
			}
		//Positive test case

		};

		for (int i = 0; i < testingData.length; i++)
			this.templatedeactiveForum((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templatedeactiveForum(final String username, final String forum, final Class<?> expected) {

		Class<?> caught = null;

		try {

			this.authenticate(username);

			final Forum aux = this.groupService.findOne(this.getEntityId(forum));
			this.groupService.deactivateGroup(aux);

			this.groupService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
}
