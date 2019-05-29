package controllers;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FilmService;
import services.GenreService;
import domain.Actor;
import domain.Genre;

@Controller
@RequestMapping("/genre/moderator")
public class GenreController extends AbstractController {

	// Services

	@Autowired
	private GenreService genreService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private FilmService filmService;

	/* Listing */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView res;
		Actor principal;
		Collection<Genre> genres;
		Boolean err;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"MODERATOR"));

			genres = this.genreService.findAll();

			res = new ModelAndView("genre/list");
			res.addObject("genres", genres);
		} catch (Throwable oopsie) {
			res = new ModelAndView("genre/list");
			err = true;

			res.addObject("errMsg", oopsie);
			res.addObject("err", err);
		}
		return res;
	}

	/* Creating a genre */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		Actor principal;
		Genre genre;
		Boolean err;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"MODERATOR"));

			genre = this.genreService.create();

			res = this.createEditModelAndView(genre);
		} catch (Throwable oopsie) {

			res = new ModelAndView("genre/list");
			err = true;

			res.addObject("errMsg", oopsie);
			res.addObject("err", err);
		}
		return res;
	}

	/* Editing a genre */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int genreId) {
		ModelAndView result;
		Genre genre;

		genre = this.genreService.findOne(genreId);
		Assert.notNull(genre);

		result = this.createEditModelAndView(genre);
		return result;
	}

	/* Saving a genre */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Genre genre,
			@RequestParam("nameES") String nameES,
			@RequestParam("nameEN") String nameEN, BindingResult binding) {
		ModelAndView res;
		Actor principal;

		if (binding.hasErrors()) {
			res = this.createEditModelAndView(genre, binding.toString());
		} else {
			try {
				genre = this.genreService.reconstruct(genre, nameES,
						nameEN, binding);
				principal = this.actorService.findByPrincipal();
				Assert.isTrue(this.actorService.checkAuthority(principal,
						"MODERATOR"));

				this.genreService.save(genre);

				res = new ModelAndView("redirect:list.do");
			} catch (Throwable oopsie) {
				res = this.createEditModelAndView(genre,
						"genre.commit.error");
			}
		}
		return res;
	}

	/* Delete genre */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Genre genre, BindingResult binding) {
		ModelAndView res;
		Actor principal;
		Genre toDelete;

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"MODERATOR"));

			toDelete = this.genreService.findOne(genre.getId());

			//TODO: eliminar el género de las películas
			//Assert.isTrue(!this.filmService.isAssigned(toDelete));

			this.genreService.delete(toDelete);

			res = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			res = this.createEditModelAndView(
					this.genreService.findOne(genre.getId()),
					"genre.cannot.delete");
		}
		return res;
	}

	// Manage methods
	protected ModelAndView createEditModelAndView(Genre genre) {
		ModelAndView res;

		res = createEditModelAndView(genre, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(Genre genre,
			String messageCode) {
		ModelAndView res;

		res = new ModelAndView("genre/edit");
		res.addObject("genre", genre);
		res.addObject("message", messageCode);

		return res;
	}
}

