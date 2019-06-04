package controllers;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CommentService;
import services.FilmService;
import services.GroupService;
import domain.Comment;
import domain.Film;
import domain.FilmEnthusiast;
import domain.Forum;

@Controller
@RequestMapping(value = "comment/filmEnthusiast")
public class CommentController extends AbstractController {

	// Services
	@Autowired
	private CommentService commentService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private FilmService filmService;

	@Autowired
	private GroupService forumService;

	// Create

	@RequestMapping(value = "/createFilm", method = RequestMethod.GET)
	public ModelAndView createFilm() {
		ModelAndView result;
		FilmEnthusiast principal = (FilmEnthusiast) this.actorService
				.findByPrincipal();
		Collection<Film> films = this.filmService.findAll();
		Collection<Forum> forums = this.forumService.findAll();

		Comment comment = this.commentService.create();

		boolean possible = false;
		if (comment.getFilmEnthusiast().equals(principal)) {
			possible = true;
		}

		result = new ModelAndView("comment/createFilm");
		result.addObject("possible", possible);
		result.addObject("films", films);
		result.addObject("comment", comment);
		result.addObject("forums",forums);

		return result;

	}



	// POST METHODS

	@RequestMapping(value = "/createFilm", method = RequestMethod.POST, params = "save")
	public ModelAndView saveFilm(Comment comment,
			final BindingResult binding) {
		ModelAndView result;

		Collection<Film> films = this.filmService.findAll();
		Collection<Forum> forums = this.forumService.findAll();

		try {
			comment = this.commentService.reconstruct(comment, binding);
			this.commentService.save(comment);
			result = new ModelAndView("redirect:/comment/filmEnthusiast/list.do");
		}catch(ValidationException oops){
			result = new ModelAndView("comment/createFilm");
			result.addObject("comment", comment);
			result.addObject("possible", true);
			result.addObject("films", films);
			result.addObject("forums",forums);
		} catch (Throwable oops) {
			result = new ModelAndView("comment/createFilm");
			result.addObject("comment", comment);
			result.addObject("error", oops.getMessage());
			result.addObject("possible", false);
		}


		return result;
	}

	@RequestMapping(value="/list")
	public ModelAndView list(){
		ModelAndView result;



		FilmEnthusiast principal = (FilmEnthusiast) this.actorService
				.findByPrincipal();

		Collection<Comment> comments = this.commentService.getCommentsByOwner(principal.getId());

		boolean possible = false;
		for(Comment comment : comments){
			if (comment.getFilmEnthusiast().equals(principal)) {
				possible = true;
				break;
			}
		}


		result = new ModelAndView("comment/list");

		result.addObject("comments", comments);
		result.addObject("possible", possible);

		return result;

	}


	@RequestMapping(value = "/display", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Comment comment,
			final BindingResult binding) {
		ModelAndView result;

		Comment validComment = this.commentService.reconstruct(comment, binding);

		if (binding.hasErrors()) {
			result = new ModelAndView("comment/display");

			result.addObject("error", "comment.binding.error");
		} else {
			try {
				this.commentService.delete(validComment);
				result = new ModelAndView("redirect:/comment/filmEnthusiast/list.do");
			} catch (Throwable oops) {
				result = new ModelAndView("comment/list");

				result.addObject("error", "comment.commit.error");
			}
		}

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int id) {
		ModelAndView result;
		Comment comment;
		FilmEnthusiast principal = (FilmEnthusiast) this.actorService
				.findByPrincipal();
		comment = this.commentService.findOne(id);
		boolean possible = false;
		if (comment.getFilmEnthusiast().equals(principal)) {
			possible = true;
		}

		result = new ModelAndView("comment/display");
		result.addObject("comment", comment);
		result.addObject("possible", possible);

		return result;
	}


}