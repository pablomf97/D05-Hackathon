
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
import domain.Actor;
import domain.Film;

@Controller
@RequestMapping("/film")
public class FilmController extends AbstractController {

	@Autowired
	private ActorService	actorService;

	@Autowired
	private FilmService		filmService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int filmId) {
		ModelAndView result;
		Film film;
		boolean isPrincipal = false;
		Actor principal;

		try {
			film = this.filmService.findOne(filmId);
			try {
				principal = this.actorService.findByPrincipal();
				if (this.actorService.checkAuthority(principal, "MODERATOR"))
					isPrincipal = true;
			} catch (final Throwable oops) {}

			result = new ModelAndView("film/display");
			result.addObject("film", film);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "film/display.do?filmId=" + filmId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "position.commit.error");
			result.addObject("permission", false);
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("film/list");
		Collection<Film> films;
		Actor principal;
		boolean isPrincipal = false;

		try {
			principal = this.actorService.findByPrincipal();
			if (this.actorService.checkAuthority(principal, "MODERATOR"))
				isPrincipal = true;

			films = this.filmService.findAll();

			result.addObject("films", films);
			result.addObject("isPrincipal", isPrincipal);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops);
			result.addObject("isPrincipal", isPrincipal);
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = null;
		try {
			final Film film = this.filmService.create();

			result = this.createEditModelAndView(film);
		} catch (final Throwable oops) {
			System.out.println(oops.getMessage());
		}
		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int filmId) {
		ModelAndView result;
		Film film;
		try {
			film = this.filmService.findOne(filmId);
			Assert.notNull(film);

			result = this.createEditModelAndView(film);
			result.addObject("filmId", filmId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Film film, final BindingResult binding) {
		ModelAndView result;
		Film aux;
		try {
			aux = this.filmService.reconstruct(film, binding);
			if (binding.hasErrors()) {

				result = new ModelAndView("film/edit");
				result.addObject("film", film);
				result.addObject("binding", binding);
				result.addObject("isPrincipal", true);
			} else
				try {
					this.filmService.save(aux);
					result = new ModelAndView("redirect:list.do");
				} catch (final Throwable oops) {
					result = new ModelAndView("film/edit");
					result.addObject("film", aux);
					result.addObject("messageCode", oops.getMessage());
				}
		} catch (final Throwable oops) {
			if (binding.hasErrors())
				result = this.createEditModelAndView(film, "jpa.error");
			else
				result = this.createEditModelAndView(film, "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int filmId) {
		ModelAndView result;
		try {
			final Film film = this.filmService.findOne(filmId);
			this.filmService.delete(film);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", oops.getMessage());
		}
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Film film) {
		ModelAndView result;

		result = this.createEditModelAndView(film, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Film film, final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isPrincipal = true;

		if (messageCode == null) {
			principal = this.actorService.findByPrincipal();

			if (!this.actorService.checkAuthority(principal, "MODERATOR"))
				isPrincipal = false;
		}

		result = new ModelAndView("film/edit");
		result.addObject("film", film);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("message", messageCode);

		return result;
	}
}
