package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SocialProfileService;
import domain.Actor;
import domain.SocialProfile;

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

		socialProfile = this.socialProfileService.findOne(id);
		res = this.createEditModelAndView(socialProfile);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SocialProfile socialProfile, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(socialProfile);
		else
			try {
				this.socialProfileService.save(socialProfile);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(SocialProfile socialProfile,
			BindingResult binding) {
		ModelAndView res;

		this.validator.validate(socialProfile, binding);

		if (binding.hasErrors()) {
			res = createEditModelAndView(socialProfile);
		} else {
			try {
				this.socialProfileService.delete(socialProfile);
				res = new ModelAndView("redirect:/");

			} catch (Throwable oops) {
				res = createEditModelAndView(socialProfile,
						"admin.commit.error");
			}
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