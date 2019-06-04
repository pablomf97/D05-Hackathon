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
import services.AdministratorService;
import domain.Actor;
import domain.Administrator;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	/* Services */

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private ActorService actorService;

	/* Methods */

	/**
	 * 
	 * Display admin
	 * 
	 * @params id (optional)
	 * 
	 * @return ModelAndView
	 * **/
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView res;
		Administrator toDisplay;
		String requestURI = "administrator/display.do";
		Boolean found = true;
		Boolean permission;

		try {
			if (id != null) {
				toDisplay = (Administrator) this.actorService.findOne(id);
				requestURI += "?id=" + id;
				if (toDisplay == null)
					found = false;
				permission = (toDisplay.getId() == this.actorService
						.findByPrincipal().getId()) ? true : false;
			} else {
				toDisplay = (Administrator) this.actorService.findByPrincipal();
				permission = true;
			}

			res = new ModelAndView("administrator/display");
			res.addObject("admin", toDisplay);
			res.addObject("found", found);
			res.addObject("requestURI", requestURI);
			res.addObject("permission", permission);
		} catch (final Throwable oops) {
			found = false;
			res = new ModelAndView("administrator/display");
			res.addObject("found", found);
		}

		return res;
	}

	/**
	 * 
	 * Register administrator GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/administrator/register", method = RequestMethod.GET)
	public ModelAndView registerNewAdministrator() {
		ModelAndView res;

		final RegisterFormObject registerFormObject = new RegisterFormObject();
		registerFormObject.setTermsAndConditions(false);

		res = this.createRegisterModelAndView(registerFormObject);

		return res;
	}

	/**
	 * 
	 * Register administrator POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/administrator/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(
			@Valid final RegisterFormObject registerFormObject,
			final BindingResult binding) {

		ModelAndView res;

		Administrator administrator = new Administrator();
		administrator = this.administratorService.create();

		administrator = this.administratorService.reconstruct(
				registerFormObject, binding);

		if (binding.hasErrors())
			res = this.createRegisterModelAndView(registerFormObject);
		else
			try {

				this.administratorService.save(administrator);

				res = new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				res = this.createRegisterModelAndView(registerFormObject,
						"administrator.commit.error");

			}
		return res;
	}

	/**
	 * 
	 * Edit administrator GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/administrator/edit", method = RequestMethod.GET)
	public ModelAndView editAdministrator() {
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
	 * Edit administrator POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/administrator/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final EditionFormObject editionFormObject,
			final BindingResult binding) {

		ModelAndView res;

		try {

			Assert.isTrue(this.actorService.findByPrincipal().getId() == editionFormObject
					.getId()
					&& this.actorService.findOne(this.actorService
							.findByPrincipal().getId()) != null);

			Administrator administrator = new Administrator();
			administrator = this.administratorService.create();

			administrator = this.administratorService.reconstruct(
					editionFormObject, binding);

			if (binding.hasErrors()) {
				res = this.createEditModelAndView(editionFormObject);
			} else {
				try {
					this.administratorService.save(administrator);

					res = new ModelAndView("redirect:/");

				} catch (Throwable oops) {
					res = this.createEditModelAndView(editionFormObject,
							"administrator.commit.error");

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

		result = new ModelAndView("administrator/register");
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

		result = new ModelAndView("administrator/edit");
		result.addObject("editionFormObject", editionFormObject);
		result.addObject("message", messageCode);

		return result;
	}

	@RequestMapping(value = "/administrator/edit", method = RequestMethod.POST, params = "deleteAdmin")
	public ModelAndView deleteAdministrator(
			final EditionFormObject editionFormObject,
			final BindingResult binding, final HttpSession session) {
		ModelAndView result;
		Administrator administrator;

		administrator = this.administratorService.findOne(editionFormObject
				.getId());
		final int count = this.administratorService.findAll().size();

		if (binding.hasErrors() || count < 2)
			result = this.createEditModelAndView(editionFormObject,
					"administrator.commit.error");
		else
			try {

				this.administratorService.deleteAdmin(administrator);
				session.invalidate();
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(editionFormObject,
						"administrator.commit.error");
			}
		return result;
	}

}
