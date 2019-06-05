
package controllers;

import java.util.ArrayList;
import java.util.Collection;

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
import domain.Actor;
import domain.Comment;
import domain.Film;
import domain.FilmEnthusiast;
import domain.Forum;

@Controller
@RequestMapping(value = "comment/filmEnthusiast")
public class CommentController extends AbstractController {

	// Services
	@Autowired
	private CommentService	commentService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private FilmService		filmService;

	@Autowired
	private GroupService	forumService;


	// Create

	@RequestMapping(value = "/createComment", method = RequestMethod.GET)
	public ModelAndView createComment() {
		ModelAndView result;
		
		try {
			FilmEnthusiast principal = (FilmEnthusiast) this.actorService
					.findByPrincipal();
			Collection<Film> films = this.filmService.publishedFilms();
			Collection<Forum> forums = this.forumService.findAllByFilmEnthusiast(principal.getId());

			Comment comment = this.commentService.create();

			boolean possible = false;
			if (comment.getFilmEnthusiast().equals(principal)) {
				possible = true;
			}

			result = new ModelAndView("comment/createComment");
			result.addObject("possible", possible);
			result.addObject("films", films);
			result.addObject("comment", comment);
			result.addObject("forums",forums);
			
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "comment.commit.error");
			result.addObject("permission", false);
		}
		

		return result;

	}

	// POST METHODS

	@RequestMapping(value = "/createComment", method = RequestMethod.POST, params = "save")
	public ModelAndView saveFilm(Comment comment,
			final BindingResult binding) {
		ModelAndView result = new ModelAndView("redirect:/welcome/index.do");;

		try {
			comment = this.commentService.reconstruct(comment, binding);
			
			if(binding.hasErrors()) {
				
				Collection<Film> films = this.filmService.publishedFilms();
				Collection<Forum> forums = this.forumService.findAllByFilmEnthusiast(comment.getFilmEnthusiast().getId());
				
				result = new ModelAndView("comment/createComment");
				result.addObject("comment", comment);
				result.addObject("binding", binding);
				result.addObject("possible", true);
				result.addObject("films", films);
				result.addObject("forums",forums);
			} else {
				try {
					this.commentService.save(comment);
					result = new ModelAndView("redirect:/comment/filmEnthusiast/list.do");
				} catch (Throwable oops) {
					result = new ModelAndView("comment/createComment");
					result.addObject("comment", comment);
					result.addObject("error", oops.getMessage());
					result.addObject("possible", false);				
				}
			}
		} catch (Throwable oops) {}

		return result;
	}

	@RequestMapping(value="/list")
	public ModelAndView list(){
		ModelAndView result = new ModelAndView("comment/list");
		boolean possible = false;
		Actor principal;
		Collection<Comment> comments = new ArrayList<>();
		
		try {
			principal = this.actorService.findByPrincipal();

			if(this.actorService.checkAuthority(principal, "FILMENTHUSIAST")) {
				comments = this.commentService.getCommentsByOwner(principal.getId());
				possible = true;
				
				result.addObject("requestURI", "comment/filmEnthusiast/list.do");
				result.addObject("comments", comments);
				result.addObject("possible", possible);
			} else {
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("possible", false);
		}
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Comment comment, final BindingResult binding) {
		ModelAndView result;

		final Comment validComment = this.commentService.reconstruct(comment, binding);

		if (binding.hasErrors()) {
			result = new ModelAndView("comment/display");

			result.addObject("error", "comment.binding.error");
		} else
			try {
				this.commentService.delete(validComment);
				result = new ModelAndView("redirect:/comment/filmEnthusiast/list.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("comment/list");

				result.addObject("error", "comment.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int id) {
		ModelAndView result;
		Comment comment;
		
		try {
			final Actor principal = this.actorService.findByPrincipal();
			comment = this.commentService.findOne(id);
			boolean possible = false;
			if (comment.getFilmEnthusiast().equals((FilmEnthusiast) principal))
				possible = true;

			result = new ModelAndView("comment/display");
			result.addObject("comment", comment);
			result.addObject("possible", possible);
		} catch (Throwable e) {
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("possible", false);
		}
		return result;
	}


}
