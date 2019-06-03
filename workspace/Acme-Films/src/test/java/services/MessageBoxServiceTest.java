package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.MessageBox;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MessageBoxServiceTest extends AbstractTest{

	@Autowired
	private MessageBoxService messageBoxService;

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
	public void driverCreate() {
		Object testingData[][] = { { "admin", "new box","inBoxAdmin1",
			null },
			// Positive test case
			{ "admin", null ,"inBoxAdmin1",
				IllegalArgumentException.class },
				// Positive test case

		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateCreate((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],

					(Class<?>) testingData[i][3]);
		}

	}

	protected void templateCreate(String username, String name,
			String parentBox, Class<?> expected) {

		Class<?> caught = null;

		try {

			this.authenticate(username);

			MessageBox mb = this.messageBoxService.create();

			mb.setName(name);
			mb.setParentMessageBox(this.messageBoxService.findOne(this.getEntityId(parentBox)));

			this.messageBoxService.save(mb);

			this.unauthenticate();

		} catch (Throwable oops) {

			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverDelete() {
		Object testingData[][] = { { "admin", "new box",null,
			null },
			// Positive test case
			{ "admin", null ,"inBoxAdmin1",
				IllegalArgumentException.class },
				// Negative test case, can't delete predefined box

		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateDelete((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],

					(Class<?>) testingData[i][3]);
		}

	}

	protected void templateDelete(String username, String name,
			String box, Class<?> expected) {

		Class<?> caught = null;

		try {

			if(box == null){
				this.authenticate(username);

				MessageBox mb = this.messageBoxService.create();

				mb.setName(name);

				this.messageBoxService.save(mb);

				this.messageBoxService.delete(mb);

				this.unauthenticate();
			}else{
				this.authenticate(username);

				MessageBox mb = this.messageBoxService.findOne(this.getEntityId(box));

				this.messageBoxService.delete(mb);


				this.unauthenticate();
			}

		} catch (Throwable oops) {

			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
