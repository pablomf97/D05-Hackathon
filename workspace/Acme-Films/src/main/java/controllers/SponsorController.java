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
import services.SponsorService;
import domain.Actor;
import domain.Sponsor;
import forms.EditionFormObject;
import forms.RegisterFormObject;

@Controller
@RequestMapping("/sponsor")
public class SponsorController extends AbstractController {

	/* Services */

	@Autowired
	private SponsorService sponsorService;

	@Autowired
	private ActorService actorService;

	/* Methods */

	/**
	 * 
	 * Display sponsor
	 * 
	 * @params id (optional)
	 * 
	 * @return ModelAndView
	 * **/
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer id) {
		ModelAndView res;
		Sponsor toDisplay;
		String requestURI = "sponsor/display.do";
		Boolean found = true;
		Boolean permission;

		try {
			if (id != null) {
				toDisplay = (Sponsor) this.actorService.findOne(id);
				if (toDisplay == null)
					found = false;
				permission = (toDisplay.getId() == this.actorService
						.findByPrincipal().getId()) ? true : false;
				requestURI += "?id=" + id;
			} else {
				toDisplay = (Sponsor) this.actorService.findByPrincipal();
				permission = true;
			}

			res = new ModelAndView("sponsor/display");
			res.addObject("sponsor", toDisplay);
			res.addObject("found", found);
			res.addObject("requestURI", requestURI);
			res.addObject("permission", permission);
		} catch (final Throwable oops) {
			found = false;
			res = new ModelAndView("sponsor/display");
			res.addObject("found", found);
		}

		return res;
	}

	/**
	 * 
	 * Register sponsor GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerNewSponsor() {
		ModelAndView res;

		final RegisterFormObject registerFormObject = new RegisterFormObject();
		registerFormObject.setTermsAndConditions(false);

		res = this.createRegisterModelAndView(registerFormObject);

		return res;
	}

	/**
	 * 
	 * Register sponsor POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView register(
			@Valid final RegisterFormObject registerFormObject,
			final BindingResult binding) {

		ModelAndView res;

		Sponsor sponsor = new Sponsor();
		sponsor = this.sponsorService.create();

		sponsor = this.sponsorService.reconstruct(registerFormObject, binding);

		if (binding.hasErrors())
			res = this.createRegisterModelAndView(registerFormObject);
		else
			try {

				this.sponsorService.save(sponsor);

				res = new ModelAndView("redirect:/");

			} catch (final Throwable oops) {
				res = this.createRegisterModelAndView(registerFormObject,
						"sponsor.commit.error");

			}
		return res;
	}

	/**
	 * 
	 * Edit sponsor GET
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/sponsor/edit", method = RequestMethod.GET)
	public ModelAndView editSponsor() {
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
	 * Edit sponsor POST
	 * 
	 * @return ModelAndView
	 **/
	@RequestMapping(value = "/sponsor/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final EditionFormObject editionFormObject,
			final BindingResult binding) {

		ModelAndView res;

		try {

			Assert.isTrue(this.actorService.findByPrincipal().getId() == editionFormObject
					.getId()
					&& this.actorService.findOne(this.actorService
							.findByPrincipal().getId()) != null);

			Sponsor sponsor = new Sponsor();
			sponsor = this.sponsorService.create();

			sponsor = this.sponsorService.reconstruct(editionFormObject,
					binding);

			if (binding.hasErrors()) {
				res = this.createEditModelAndView(editionFormObject);
			} else {
				try {
					this.sponsorService.save(sponsor);

					res = new ModelAndView("redirect:/");

				} catch (Throwable oops) {
					res = this.createEditModelAndView(editionFormObject,
							"sponsor.commit.error");

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

		result = new ModelAndView("sponsor/register");
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

		result = new ModelAndView("sponsor/edit");
		result.addObject("editionFormObject", editionFormObject);
		result.addObject("message", messageCode);

		return result;
	}

	@RequestMapping(value = "/sponsor/edit", method = RequestMethod.POST, params = "deleteSponsor")
	public ModelAndView deleteSponsor(
			final EditionFormObject editionFormObject,
			final BindingResult binding, final HttpSession session) {
		ModelAndView result;
		Sponsor sponsor;

		sponsor = this.sponsorService.findOne(editionFormObject.getId());

		if (binding.hasErrors())
			result = this.createEditModelAndView(editionFormObject,
					"sponsor.commit.error");
		else
			try {

				this.sponsorService.deleteSponsor(sponsor);
				session.invalidate();
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(editionFormObject,
						"sponsor.commit.error");
			}
		return result;
	}
}
