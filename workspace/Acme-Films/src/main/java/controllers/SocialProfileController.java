package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SocialProfileService;
import domain.Actor;
import domain.Administrator;
import domain.Critic;
import domain.Moderator;
import domain.SocialProfile;
import domain.Sponsor;

@Controller
@RequestMapping("/social/actor")
public class SocialProfileController extends AbstractController {

	// Services

	@Autowired
	private ActorService actorService;

	@Autowired
	private SocialProfileService socialProfileService;

	@Autowired
	private Validator validator;

	// Constructors

	public SocialProfileController() {
		super();
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;
		SocialProfile socialProfile;

		socialProfile = this.socialProfileService.create();
		res = this.createEditModelAndView(socialProfile);

		return res;
	}

	@RequestMapping(value = "/edit", params = "id")
	public ModelAndView display(@RequestParam int id) {
		ModelAndView res;
		SocialProfile socialProfile;

		try {
			socialProfile = this.socialProfileService.findOne(id);
			Assert.isTrue(this.actorService.findByPrincipal()
					.getSocialProfile().contains(socialProfile));

			res = this.createEditModelAndView(socialProfile);
		} catch (Throwable oops) {
			res = new ModelAndView("redirect:/welcome/index.do");
		}

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SocialProfile socialProfile, BindingResult binding) {
		ModelAndView res;

		this.validator.validate(socialProfile, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(socialProfile);
		else
			try {
				this.socialProfileService.save(socialProfile);

				Actor principal = this.actorService.findByPrincipal();
				if (principal instanceof Administrator)
					res = new ModelAndView("redirect:/administrator/display.do");
				else if (principal instanceof Sponsor)
					res = new ModelAndView("redirect:/sponsor/display.do");
				else if (principal instanceof Critic)
					res = new ModelAndView("redirect:/critic/display.do");
				else if (principal instanceof Moderator)
					res = new ModelAndView("redirect:/moderator/display.do");
				else
					res = new ModelAndView(
							"redirect:/filmEnthusiast/display.do");
			} catch (final Throwable oops) {
				res = new ModelAndView("redirect:/welcome/index.do");
			}
		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(SocialProfile socialProfile,
			BindingResult binding) {
		ModelAndView res;

		try {
			this.socialProfileService.delete(socialProfile);

			Actor principal = this.actorService.findByPrincipal();
			if (principal instanceof Administrator)
				res = new ModelAndView("redirect:/administrator/display.do");
			else if (principal instanceof Sponsor)
				res = new ModelAndView("redirect:/sponsor/display.do");
			else if (principal instanceof Critic)
				res = new ModelAndView("redirect:/critic/display.do");
			else if (principal instanceof Moderator)
				res = new ModelAndView("redirect:/moderator/display.do");
			else
				res = new ModelAndView("redirect:/filmEnthusiast/display.do");

		} catch (Throwable oops) {
			res = createEditModelAndView(socialProfile, "admin.commit.error");
		}
		return res;
	}

	// /--------ModelAndView

	protected ModelAndView createEditModelAndView(SocialProfile socialProfile) {
		ModelAndView res;

		res = this.createEditModelAndView(socialProfile, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(SocialProfile socialProfile,
			String messageCode) {
		ModelAndView res;
		Actor principal;
		principal = this.actorService.findByPrincipal();

		res = new ModelAndView("socialProfile/edit");
		res.addObject("principal", principal);
		res.addObject("socialProfile", socialProfile);
		res.addObject("message", messageCode);
		return res;
	}

}