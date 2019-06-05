
package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Event;
import domain.Forum;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EventServiceTest extends AbstractTest {

	@Autowired
	private EventService	eventService;

	@Autowired
	private GroupService	groupService;


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
	 * Manage event, which includes listing, displaying,
	 * creating, updating, and deleting it. UC 3.1,3.2
	 */

	/*
	 * ####################### TEST CREATE EVENT #######################
	 */

	@Test
	public void driverCreateEvent() {
		final Object testingData[][] = {
			{
				"filmEnthusiast1", "forum1", "address", "description", new Date(System.currentTimeMillis() + 100000), new Date(System.currentTimeMillis() + 1000000), 50, 7.2, "title", null
			},
			//Positive test case 

			{
				"moderator1", "forum1", "address", "description", new Date(System.currentTimeMillis() + 100000), new Date(System.currentTimeMillis() + 1000000), 50, 7.2, "title", IllegalArgumentException.class
			},

			//Negative test case, trying to create and event as a moderator (wrong actor).

			{
				"filmEnthusiast2", "forum1", "address", "description", new Date(System.currentTimeMillis() + 100000), new Date(System.currentTimeMillis() + 1000000), 50, 7.2, "title", IllegalArgumentException.class
			},

		//Negative test case,  trying to create and event as a other filmEnthusiast that group creator
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateEvent((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Date) testingData[i][4], (Date) testingData[i][5], (Integer) testingData[i][6],
				(Double) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	protected void templateCreateEvent(final String username, final String group, final String address, final String description, final Date eventDate, final Date signinDeadline, final Integer maximumCapacity, final Double price, final String title,
		final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Forum forum = this.groupService.findOne(this.getEntityId(group));

			final Event event = this.eventService.create(forum);

			event.setAddress(address);
			event.setDescription(description);
			event.setEventDate(eventDate);
			event.setSigninDeadline(signinDeadline);
			event.setMaximumCapacity(maximumCapacity);
			event.setPrice(price);
			event.setTitle(title);

			this.eventService.save(event);
			this.eventService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST list EVENT #######################
	 */

	@Test
	public void driverListEvent() {
		final Object testingData[][] = {

			{
				"filmEnthusiast1", "forum1", null
			},

			//Positive test case

			{
				"filmEnthusiast3", "forum3", IllegalArgumentException.class
			},

			//Negative test case, event4 doesn't correspond to filmEnthusiast 1

			{
				"moderator1", "event3", IllegalArgumentException.class
			}

		//Negative test case, wrong actor
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListEvent((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateListEvent(final String username, final String group, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Forum aux = this.groupService.findOne(this.getEntityId(group));

			this.eventService.findAllByGroup(aux.getId());
			this.eventService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST DELETE EVENT #######################
	 */

	@Test
	public void driverDeleteEvent() {
		final Object testingData[][] = {
			{
				"filmEnthusiast1", "event1", null
			},
			//Positive test case

			{
				"filmEnthusiast1", "event2", IllegalArgumentException.class
			},
			//Negative test case, filmEnthusiast1 is not owner of the group

			{
				"filmEnthusiast1", "event200", IllegalArgumentException.class
			},
			//Negative test case, event4 doesn't exist

			{
				"moderator1", "event5", IllegalArgumentException.class
			},
			//Negative test case, wrong actor

			{
				null, "event1", IllegalArgumentException.class
			}
		//Negative test case, null actor

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteEvent((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateDeleteEvent(final String username, final String event, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Event aux = this.eventService.findOne(this.getEntityId(event));

			this.eventService.delete(aux);

			this.eventService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST request EVENT #######################
	 */

	@Test
	public void driverRequestEvent() {
		final Object testingData[][] = {
			{
				"filmEnthusiast1", "event2", null
			},
			//Positive test case

			{
				"filmEnthusiast1", "event1", IllegalArgumentException.class
			},
			//Negative test case, filmEnthusiast1 is creator of the event

			{
				"filmEnthusiast2", "event4", IllegalArgumentException.class
			},
			//Negative test case, the group of the event4 doesn't correspond to filmEnthusiast1

			{
				"moderator1", "event1", IllegalArgumentException.class
			},
			//Negative test case, wrong actor

			{
				null, "event1", IllegalArgumentException.class
			}
		//Negative test case, null actor

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRequestEvent((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	protected void templateRequestEvent(final String username, final String event, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);

			final Event aux = this.eventService.findOne(this.getEntityId(event));

			this.eventService.requestEvent(aux.getId());

			this.eventService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
