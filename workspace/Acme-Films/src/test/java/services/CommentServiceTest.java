package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Comment;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CommentServiceTest extends AbstractTest{
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private FilmService filmService;
	
	@Autowired
	private GroupService forumService;

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
	public void driverComment() {
		Object testingData[][] = { { "filmEnthusiast1", "body", 2.0, "film1","forum1",
				null },
				// Positive test case
				{ "filmEnthusiast1", "body", 2.0, "","",
					IllegalArgumentException.class },
				//Negative test case , need to comment a film or a group
				
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateComment((String) testingData[i][0],
					(String) testingData[i][1], (Double) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4],
					(Class<?>) testingData[i][5]);
		}

	}

	protected void templateComment(String username, String body,
			Double rating, String film, String forum, Class<?> expected) {

		Class<?> caught = null;

		try {

			this.authenticate(username);
			
			Comment comment = this.commentService.create();

			comment.setBody(body);
			comment.setRating(rating);
			comment.setFilm(this.filmService.findOne(this.getEntityId(film)));
			comment.setForum(this.forumService.findOne(this.getEntityId(forum)));
			

			this.commentService.save(comment);

			this.unauthenticate();

		} catch (Throwable oops) {

			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	@Test
	public void driverDelete() {
		Object testingData[][] = { { "filmEnthusiast1", "comment1",null },
				// Positive test case
				{ "critic1", "comment1",ClassCastException.class},
				//Negative test case , wrong actor
				
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateDelete((String) testingData[i][0],
					 (String) testingData[i][1],
					(Class<?>) testingData[i][2]);
		}

	}

	protected void templateDelete(String username, String comment, Class<?> expected) {

		Class<?> caught = null;

		try {

			this.authenticate(username);
			
			Comment c = this.commentService.findOne(this.getEntityId(comment));

			
			

			this.commentService.delete(c);

			this.unauthenticate();

		} catch (Throwable oops) {

			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	
}
