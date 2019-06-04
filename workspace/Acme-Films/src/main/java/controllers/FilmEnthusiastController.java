package controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FilmEnthusiastService;
import services.FinderService;
import domain.Actor;
import domain.FilmEnthusiast;
import domain.Finder;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Controller
@RequestMapping("/filmEnthusiast")
public class FilmEnthusiastController extends AbstractController {

	/* Services */

	@Autowired
	private FilmEnthusiastService filmEnthusiastService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private FinderService finderService;

	/* Methods */

	/**
	 * 
	 * Display filmEnthusiast
	 * 
	 * @params id (optional)
	 * 
	 * @return ModelAndView
	 * **/
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView res;
		FilmEnthusiast toDisplay;
		String requestURI = "filmEnthusiast/display.do";
		Boolean found = true;
		Boolean permission = false;

		try {
			if (id != null) {
				toDisplay = (FilmEnthusiast) this.actorService.findOne(id);
				if (toDisplay == null)
					found = false;
				if (this.actorService.findByPrincipal() != null) {
					permission = (toDisplay.getId() == this.actorService
							.findByPrincipal().getId()) ? true : false;
				}
				requestURI += "?id=" + id;
			} else {
				toDisplay = (FilmEnthusiast) this.actorService
						.findByPrincipal();
				permission = true;
			}

			res = new ModelAndView("filmEnthusiast/display");
			res.addObject("filmEnthusiast", toDisplay);
			res.addObject("found", found);
			res.addObject("requestURI", requestURI);
			res.addObject("permission", permission);
		} catch (final Throwable oops) {
			found = false;
			res = new ModelAndView("filmEnthusiast/display");
			res.addObject("found", found);
		}

		return res;
	}

	/**
	 * 
	 * Register filmEnthusiast GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerNewFilmEnthusiast() {
		ModelAndView res;

		final RegisterFormObject registerFormObject = new RegisterFormObject();
		registerFormObject.setTermsAndConditions(false);

		res = this.createRegisterModelAndView(registerFormObject);

		return res;
	}

	/**
	 * 
	 * Register filmEnthusiast POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(
			@Valid final RegisterFormObject registerFormObject,
			final BindingResult binding) {

		ModelAndView res;

		FilmEnthusiast filmEnthusiast = new FilmEnthusiast();
		filmEnthusiast = this.filmEnthusiastService.create();

		filmEnthusiast = this.filmEnthusiastService.reconstruct(
				registerFormObject, binding);

		if (binding.hasErrors())
			res = this.createRegisterModelAndView(registerFormObject);
		else
			try {
				Finder finder = this.finderService.create();
				finder = this.finderService.save(finder);

				filmEnthusiast.setFinder(finder);

				this.filmEnthusiastService.save(filmEnthusiast);

				res = new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				res = this.createRegisterModelAndView(registerFormObject,
						"filmEnthusiast.commit.error");

			}
		return res;
	}

	/**
	 * 
	 * Edit filmEnthusiast GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/filmEnthusiast/edit", method = RequestMethod.GET)
	public ModelAndView editFilmEnthusiast() {
		ModelAndView res;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		final EditionFormObject editionFormObject = new EditionFormObject(
				principal);

		res = this.createEditModelAndView(editionFormObject);

		return res;
	}

	/**
	 * 
	 * Edit filmEnthusiast POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/filmEnthusiast/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final EditionFormObject editionFormObject,
			final BindingResult binding) {

		ModelAndView res;

		try {

			Assert.isTrue(this.actorService.findByPrincipal().getId() == editionFormObject
					.getId()
					&& this.actorService.findOne(this.actorService
							.findByPrincipal().getId()) != null);

			FilmEnthusiast filmEnthusiast = new FilmEnthusiast();
			filmEnthusiast = this.filmEnthusiastService.create();

			filmEnthusiast = this.filmEnthusiastService.reconstruct(
					editionFormObject, binding);

			if (binding.hasErrors()) {
				res = this.createEditModelAndView(editionFormObject);
			} else {
				try {
					this.filmEnthusiastService.save(filmEnthusiast);

					res = new ModelAndView("redirect:/");

				} catch (Throwable oops) {
					res = this.createEditModelAndView(editionFormObject,
							"filmEnthusiast.commit.error");

				}

			}

		} catch (Throwable oops) {
			res = new ModelAndView("redirect:/");
		}

		return res;
	}

	/* Auxiliary methods */

	/* Registration related */
	protected ModelAndView createRegisterModelAndView(
			final RegisterFormObject registerFormObject) {
		ModelAndView result;

		result = this.createRegisterModelAndView(registerFormObject, null);

		return result;
	}

	protected ModelAndView createRegisterModelAndView(
			final RegisterFormObject registerFormObject,
			final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("filmEnthusiast/register");
		result.addObject("registerFormObject", registerFormObject);
		result.addObject("message", messageCode);

		return result;
	}

	/* Edition related */
	protected ModelAndView createEditModelAndView(
			final EditionFormObject editionFormObject) {
		ModelAndView result;

		result = this.createEditModelAndView(editionFormObject, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(
			final EditionFormObject editionFormObject, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("filmEnthusiast/edit");
		result.addObject("editionFormObject", editionFormObject);
		result.addObject("message", messageCode);

		return result;
	}

	@RequestMapping(value = "/filmEnthusiast/edit", method = RequestMethod.POST, params = "deleteFilmEnthusiast")
	public ModelAndView deleteFilmEnthusiast(
			final EditionFormObject editionFormObject,
			final BindingResult binding, final HttpSession session) {
		ModelAndView result;
		FilmEnthusiast filmEnthusiast;

		filmEnthusiast = this.filmEnthusiastService.findOne(editionFormObject
				.getId());

		if (binding.hasErrors())
			result = this.createEditModelAndView(editionFormObject,
					"filmEnthusiast.commit.error");
		else
			try {

				this.filmEnthusiastService.deleteFilmEnthusiast(filmEnthusiast);
				session.invalidate();
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(editionFormObject,
						"filmEnthusiast.commit.error");
			}
		return result;
	}
}
