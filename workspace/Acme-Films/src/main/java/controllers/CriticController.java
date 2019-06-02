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
import services.CriticService;
import domain.Actor;
import domain.Critic;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Controller
@RequestMapping("/critic")
public class CriticController extends AbstractController {

	/* Services */

	@Autowired
	private CriticService criticService;

	@Autowired
	private ActorService actorService;

	/* Methods */

	/**
	 * 
	 * Display critic
	 * 
	 * @params id (optional)
	 * 
	 * @return ModelAndView
	 * **/
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView res;
		Critic toDisplay;
		String requestURI = "critic/display.do";
		Boolean found = true;
		Boolean permission;

		try {
			if (id != null) {
				toDisplay = (Critic) this.actorService.findOne(id);
				if (toDisplay == null)
					found = false;
				permission = (toDisplay.getId() == this.actorService
						.findByPrincipal().getId()) ? true : false;
				requestURI += "?id=" + id;
			} else {
				toDisplay = (Critic) this.actorService.findByPrincipal();
				permission = true;
			}

			res = new ModelAndView("critic/display");
			res.addObject("critic", toDisplay);
			res.addObject("found", found);
			res.addObject("requestURI", requestURI);
			res.addObject("permission", permission);
		} catch (final Throwable oops) {
			found = false;
			res = new ModelAndView("critic/display");
			res.addObject("found", found);
		}

		return res;
	}

	/**
	 * 
	 * Register critic GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerNewCritic() {
		ModelAndView res;

		final RegisterFormObject registerFormObject = new RegisterFormObject();
		registerFormObject.setTermsAndConditions(false);

		res = this.createRegisterModelAndView(registerFormObject);

		return res;
	}

	/**
	 * 
	 * Register critic POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(
			@Valid final RegisterFormObject registerFormObject,
			final BindingResult binding) {

		ModelAndView res;

		Critic critic = new Critic();
		critic = this.criticService.create();

		critic = this.criticService.reconstruct(registerFormObject, binding);

		if (binding.hasErrors())
			res = this.createRegisterModelAndView(registerFormObject);
		else
			try {

				this.criticService.save(critic);

				res = new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				res = this.createRegisterModelAndView(registerFormObject,
						"critic.commit.error");

			}
		return res;
	}

	/**
	 * 
	 * Edit critic GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/critic/edit", method = RequestMethod.GET)
	public ModelAndView editCritic() {
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
	 * Edit critic POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/critic/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final EditionFormObject editionFormObject,
			final BindingResult binding) {

		ModelAndView res;

		try {

			Assert.isTrue(this.actorService.findByPrincipal().getId() == editionFormObject
					.getId()
					&& this.actorService.findOne(this.actorService
							.findByPrincipal().getId()) != null);

			Critic critic = new Critic();
			critic = this.criticService.create();

			critic = this.criticService.reconstruct(editionFormObject,
					binding);

			if (binding.hasErrors()) {
				res = this.createEditModelAndView(editionFormObject);
			} else {
				try {
					this.criticService.save(critic);

					res = new ModelAndView("redirect:/");

				} catch (Throwable oops) {
					res = this.createEditModelAndView(editionFormObject,
							"critic.commit.error");

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

		result = new ModelAndView("critic/register");
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

		result = new ModelAndView("critic/edit");
		result.addObject("editionFormObject", editionFormObject);
		result.addObject("message", messageCode);

		return result;
	}

	@RequestMapping(value = "/critic/edit", method = RequestMethod.POST, params = "deleteCritic")
	public ModelAndView deleteCritic(
			final EditionFormObject editionFormObject,
			final BindingResult binding, final HttpSession session) {
		ModelAndView result;
		Critic critic;

		critic = this.criticService.findOne(editionFormObject.getId());

		if (binding.hasErrors())
			result = this.createEditModelAndView(editionFormObject,
					"critic.commit.error");
		else
			try {

				this.criticService.DeleteCritic(critic);
				session.invalidate();
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(editionFormObject,
						"critic.commit.error");
			}
		return result;
	}
}
