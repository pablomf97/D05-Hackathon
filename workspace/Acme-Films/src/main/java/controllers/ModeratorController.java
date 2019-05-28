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
import services.ModeratorService;
import domain.Actor;
import domain.Moderator;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Controller
@RequestMapping("/moderator")
public class ModeratorController extends AbstractController {

	/* Services */

	@Autowired
	private ModeratorService moderatorService;

	@Autowired
	private ActorService actorService;

	/* Methods */

	/**
	 * 
	 * Display moderator
	 * 
	 * @params id (optional)
	 * 
	 * @return ModelAndView
	 * **/
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView res;
		Moderator toDisplay;
		String requestURI = "moderator/display.do";
		Boolean found = true;
		Boolean permission = false;

		try {
			if (id != null) {
				toDisplay = (Moderator) this.actorService.findOne(id);
				requestURI += "?id=" + id;
				if (toDisplay == null)
					found = false;
			} else {
				toDisplay = (Moderator) this.actorService.findByPrincipal();
				permission = true;
			}

			res = new ModelAndView("moderator/display");
			res.addObject("moderator", toDisplay);
			res.addObject("found", found);
			res.addObject("requestURI", requestURI);
			res.addObject("permission", permission);
		} catch (final Throwable oops) {
			found = false;
			res = new ModelAndView("moderator/display");
			res.addObject("found", found);
		}

		return res;
	}

	/**
	 * 
	 * Register moderator GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/administrator/register", method = RequestMethod.GET)
	public ModelAndView registerNewModerator() {
		ModelAndView res;

		final RegisterFormObject registerFormObject = new RegisterFormObject();
		registerFormObject.setTermsAndConditions(false);

		res = this.createRegisterModelAndView(registerFormObject);

		return res;
	}

	/**
	 * 
	 * Register moderator POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/administrator/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(
			@Valid final RegisterFormObject registerFormObject,
			final BindingResult binding) {

		ModelAndView res;

		Moderator moderator = new Moderator();
		moderator = this.moderatorService.create();

		moderator = this.moderatorService.reconstruct(registerFormObject,
				binding);

		if (binding.hasErrors())
			res = this.createRegisterModelAndView(registerFormObject);
		else
			try {

				this.moderatorService.save(moderator);

				res = new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				res = this.createRegisterModelAndView(registerFormObject,
						"moderator.commit.error");

			}
		return res;
	}

	/**
	 * 
	 * Edit moderator GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/moderator/edit", method = RequestMethod.GET)
	public ModelAndView editModerator() {
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
	 * Edit moderator POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/moderator/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final EditionFormObject editionFormObject,
			final BindingResult binding) {

		ModelAndView res;

		try {

			Assert.isTrue(this.actorService.findByPrincipal().getId() == editionFormObject
					.getId()
					&& this.actorService.findOne(this.actorService
							.findByPrincipal().getId()) != null);

			Moderator moderator = new Moderator();
			moderator = this.moderatorService.create();

			moderator = this.moderatorService.reconstruct(editionFormObject,
					binding);

			if (binding.hasErrors()) {
				res = this.createEditModelAndView(editionFormObject);
			} else {
				try {
					this.moderatorService.save(moderator);

					res = new ModelAndView("redirect:/");

				} catch (Throwable oops) {
					res = this.createEditModelAndView(editionFormObject,
							"moderator.commit.error");

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

		result = new ModelAndView("moderator/register");
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

		result = new ModelAndView("moderator/edit");
		result.addObject("editionFormObject", editionFormObject);
		result.addObject("message", messageCode);

		return result;
	}

	@RequestMapping(value = "/moderator/edit", method = RequestMethod.POST, params = "deleteModerator")
	public ModelAndView deleteModerator(
			final EditionFormObject editionFormObject,
			final BindingResult binding, final HttpSession session) {
		ModelAndView result;
		Moderator moderator;

		moderator = this.moderatorService.findOne(editionFormObject.getId());

		if (binding.hasErrors())
			result = this.createEditModelAndView(editionFormObject,
					"moderator.commit.error");
		else
			try {

				this.moderatorService.delete(moderator);
				session.invalidate();
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(editionFormObject,
						"moderator.commit.error");
			}
		return result;
	}
}
