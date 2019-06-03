package services;

import java.util.ConcurrentModificationException;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;

import services.MessageService;
import services.ActorService;

import domain.Message;

import domain.Actor;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService messageService;

	@Autowired
	private ActorService actorService;

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
	 * 
	 * During this test we are going to try:
	 * 
	 * 9.3. Exchange messages with other actors and manage them.
	 * 
	 * 
	 * /* ####################### TEST EXCHANGE MESSAGES #######################
	 */

	@Test
	public void driverExchangeMessage() {
		Object testingData[][] = { { "admin", "test", "test", "critic1",
				"HIGH", null },
				// Positive test case
				{"admin", null , "test", "critic1",
					"HIGH", IllegalArgumentException.class}
				//Not null subject

		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateExchangeMessages((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4],
					(Class<?>) testingData[i][5]);
		}

	}

	protected void templateExchangeMessages(String username, String subject,
			String body, String receiver, String priority, Class<?> expected) {

		Class<?> caught = null;

		try {

			this.authenticate(username);
			Actor recipient = this.actorService.findOne(this
					.getEntityId(receiver));

			Message message = this.messageService.create();

			message.setSubject(subject);
			message.setBody(body);
			message.setReceiver(recipient);
			message.setPriority(priority);

			this.messageService.save(message);

			this.unauthenticate();

		} catch (Throwable oops) {

			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverBroadcast() {
		Object testingData[][] = { { "admin", "test", "test", "HIGH", null },
				// Positive test case
				{ "critic1", "test", "test", "HIGH", IllegalArgumentException.class }
				//Only admin can send broadcast message

		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateBroadcast((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (Class<?>) testingData[i][4]);
		}

	}

	protected void templateBroadcast(String username, String subject,
			String body, String priority, Class<?> expected) {

		Class<?> caught = null;

		try {

			this.authenticate(username);

			Message message = this.messageService.create();

			message.setSubject(subject);
			message.setBody(body);
			message.setPriority(priority);

			this.messageService.broadcast(message);

			this.unauthenticate();

		} catch (Throwable oops) {

			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	protected void templateExchangeMessages1(String username, String subject,
			String body, String receiver, String priority, Class<?> expected) {

		Class<?> caught = null;

		try {

			this.authenticate(username);
			Actor recipient = this.actorService.findOne(this
					.getEntityId(receiver));

			Message message = this.messageService.create();

			message.setSubject(subject);
			message.setBody(body);
			message.setReceiver(recipient);
			message.setPriority(priority);

			this.messageService.save(message);

			this.unauthenticate();

		} catch (Throwable oops) {

			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverDelete() {
		Object testingData[][] = { { "admin", "message1", ConcurrentModificationException.class },
				{ "admin", "message2", ConcurrentModificationException.class }
		// Positive test case

		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateDelete((String) testingData[i][0],
					 (String) testingData[i][1],
					(Class<?>) testingData[i][2]);
		}

	}

	protected void templateDelete(String username, String message, Class<?> expected) {

		Class<?> caught = null;

		try {

			this.authenticate(username);
			Message m = this.messageService.findOne(this.getEntityId(message));
			

			this.messageService.delete(m);

			this.unauthenticate();

		} catch (Throwable oops) {

			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
