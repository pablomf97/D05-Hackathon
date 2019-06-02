
package controllers;

import java.util.ArrayList;
import java.util.Collection;

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
import services.FilmService;
import services.SponsorshipService;
import domain.Actor;
import domain.Film;
import domain.Sponsor;
import domain.Sponsorship;
import forms.CreateSponsorshipFormObject;
import forms.EditSponsorshipFormObject;

@Controller
@RequestMapping("/sponsorship")
public class SponsorshipController extends AbstractController {

	@Autowired
	private ActorService	actorService;

	@Autowired
	private SponsorshipService		sponsorshipService;
	
	@Autowired
	private FilmService		filmService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;
		boolean isPrincipal = false;
		Actor principal;

		try {
			sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			try {
				principal = this.actorService.findByPrincipal();
				if (sponsorship.getSponsor().equals((Sponsor) principal));
					isPrincipal = true;
			} catch (final Throwable oops) {}

			result = new ModelAndView("sponsorship/display");
			result.addObject("sponsorship", sponsorship);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "sponsorship/display.do?sponsorshipId=" + sponsorshipId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "position.commit.error");
			result.addObject("permission", false);
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("sponsorship/list");
		Collection<Sponsorship> sponsorships = new ArrayList<>();
		Actor principal = null;
		String isPrincipal = null;

		try {
			principal = this.actorService.findByPrincipal();
			if (this.actorService.checkAuthority(principal, "SPONSOR")) {
				sponsorships = this.sponsorshipService.sponsorshipsPerSponsor(principal.getId());
				isPrincipal = "SPONSOR";
			} else if(this.actorService.checkAuthority(principal, "MODERATOR")) {
				sponsorships = this.sponsorshipService.sponsorshipsToReview();
				isPrincipal = "MODERATOR";
			}
						
			result.addObject("sponsorships", sponsorships);
			result.addObject("isPrincipal", isPrincipal);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops);
			result.addObject("isPrincipal", isPrincipal);
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createSponsorship() {
		ModelAndView res;

		final CreateSponsorshipFormObject createSponsorshipFormObject = new CreateSponsorshipFormObject();

		res = this.createNewModelAndView(createSponsorshipFormObject);
		res.addObject("isPrincipal", true);

		return res;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView res;
		Sponsorship sponsorship;
		Actor principal = this.actorService.findByPrincipal();
		boolean isPrincipal = false;

		sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		
		if(sponsorship.getSponsor().equals((Sponsor)principal)) {
			isPrincipal = true;
		}

		final EditSponsorshipFormObject editSponsorshipFormObject = new EditSponsorshipFormObject(sponsorship);

		res = this.createEditModelAndView(editSponsorshipFormObject);
		res.addObject("isPrincipal", isPrincipal);
		res.addObject("isActive", sponsorship.getIsActive());

		return res;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "newSp")
	public ModelAndView newSp(@Valid final CreateSponsorshipFormObject createSponsorshipFormObject, final BindingResult binding) {

		Actor principal;
		ModelAndView res;
		boolean isPrincipal = false;

		try {
			principal = this.actorService.findByPrincipal();
			Sponsorship sponsorship = new Sponsorship();
			sponsorship = this.sponsorshipService.create();

			sponsorship = this.sponsorshipService.reconstruct(createSponsorshipFormObject, binding);
			
			Assert.isTrue(sponsorship.getSponsor().equals((Sponsor) principal), "not.allowed");

			if (binding.hasErrors())
				res = this.createNewModelAndView(createSponsorshipFormObject);
			else
				try {

					this.sponsorshipService.save(sponsorship);

					res = new ModelAndView("redirect:list.do");

				} catch (final Throwable oops) {
					if(sponsorship.getSponsor().equals((Sponsor)principal)) {
						isPrincipal = true;
					}
					res = this.createNewModelAndView(createSponsorshipFormObject, "sponsorship.commit.error");
					res.addObject("isPrincipal", isPrincipal);

				}
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:list.do");
		}
		return res;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final EditSponsorshipFormObject editSponsorshipFormObject, final BindingResult binding) {
		Actor principal;
		ModelAndView res;
		boolean isPrincipal = false;

		try {
			principal = this.actorService.findByPrincipal();
			Sponsorship sponsorship = new Sponsorship();
			sponsorship = this.sponsorshipService.create();

			sponsorship = this.sponsorshipService.reconstruct(editSponsorshipFormObject, binding);
			
			Assert.isTrue(sponsorship.getSponsor().equals((Sponsor) principal), "not.allowed");

			if (binding.hasErrors())
				res = this.createEditModelAndView(editSponsorshipFormObject);
			else
				try {

					this.sponsorshipService.save(sponsorship);

					res = new ModelAndView("redirect:list.do");

				} catch (final Throwable oops) {
					if(sponsorship.getSponsor().equals((Sponsor)principal)) {
						isPrincipal = true;
					}
					res = this.createEditModelAndView(editSponsorshipFormObject, "sponsorship.commit.error");
					res.addObject("isPrincipal", isPrincipal);

				}
		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:list.do");
		}
		return res;
	}

//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
//	public ModelAndView save(final Sponsorship sponsorship, final BindingResult binding) {
//		ModelAndView result;
//		Sponsorship aux;
//		try {
//			aux = this.sponsorshipService.reconstruct(sponsorship, binding);
//			if (binding.hasErrors()) {
//
//				result = new ModelAndView("sponsorship/edit");
//				result.addObject("sponsorship", sponsorship);
//				result.addObject("binding", binding);
//				result.addObject("isPrincipal", true);
//			} else
//				try {
//					this.sponsorshipService.save(aux);
//					result = new ModelAndView("redirect:list.do");
//				} catch (final Throwable oops) {
//					result = new ModelAndView("sponsorship/edit");
//					result.addObject("sponsorship", aux);
//					result.addObject("messageCode", oops.getMessage());
//				}
//		} catch (final Throwable oops) {
//			if (binding.hasErrors())
//				result = this.createEditModelAndView(sponsorship, "jpa.error");
//			else
//				result = this.createEditModelAndView(sponsorship, "commit.error");
//		}
//		return result;
//	}

	/* Accept or reject an sponsorship */
	@RequestMapping(value = "/action", method = RequestMethod.GET)
	public ModelAndView actionsEnrolments(@RequestParam final String action, @RequestParam final int sponsorshipId) {
		ModelAndView res;
		Actor principal;
		Sponsorship sponsorship = null; 

		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue((this.actorService.checkAuthority(principal, "MODERATOR")) ||
					(this.actorService.checkAuthority(principal, "SPONSOR")));

			sponsorship = this.sponsorshipService.findOne(sponsorshipId);

			if((this.actorService.checkAuthority(principal, "MODERATOR"))) {
				Assert.isNull(sponsorship.getIsActive());
			}
			
			if((this.actorService.checkAuthority(principal, "SPONSOR"))) {
				Assert.isTrue(sponsorship.getIsActive());
			}
			

			if (action.equals("accept")) {

				sponsorship.setIsActive(true);
				this.sponsorshipService.save(sponsorship);
				res = new ModelAndView("redirect:/sponsorship/list.do");

			} else if (action.equals("reject")) {

				sponsorship.setIsActive(false);
				this.sponsorshipService.save(sponsorship);
				res = new ModelAndView("redirect:/sponsorship/list.do");
			} else
				res = new ModelAndView("redirect:/sponsorship/list.do");
		} catch (final Throwable oopsie) {
			res = new ModelAndView("redirect:/sponsorship/list.do");
		}
		return res;
	}	
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		try {
			final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			this.sponsorshipService.delete(sponsorship);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:welcome.do");
			result.addObject("messageCode", oops.getMessage());
		}
		return result;
	}

	// Ancillary methods
	
	protected ModelAndView createEditModelAndView(final EditSponsorshipFormObject editSponsorshipFormObject) {
		ModelAndView result;

		result = this.createEditModelAndView(editSponsorshipFormObject, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final EditSponsorshipFormObject editSponsorshipFormObject, final String messageCode) {
		ModelAndView result;
		Collection<Film> films = this.filmService.publishedFilms();
		
		result = new ModelAndView("sponsorship/edit");
		result.addObject("editSponsorshipFormObject", editSponsorshipFormObject);
		result.addObject("message", messageCode);
		result.addObject("films", films);

		return result;
	}

	protected ModelAndView createNewModelAndView(final CreateSponsorshipFormObject createSponsorshipFormObject) {
		ModelAndView result;

		result = this.createNewModelAndView(createSponsorshipFormObject, null);

		return result;
	}

	protected ModelAndView createNewModelAndView(final CreateSponsorshipFormObject createSponsorshipFormObject, final String messageCode) {
		ModelAndView result;
		Collection<Film> films = this.filmService.publishedFilms();

		result = new ModelAndView("sponsorship/create");
		result.addObject("createSponsorshipFormObject", createSponsorshipFormObject);
		result.addObject("message", messageCode);
		result.addObject("films", films);

		return result;
	}


//	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageCode) {
//		final ModelAndView result;
//		Actor principal;
//		boolean isPrincipal = true;
//
//		if (messageCode == null) {
//			principal = this.actorService.findByPrincipal();
//
//			if (!sponsorship.getSponsor().equals((Sponsor) principal))
//				isPrincipal = false;
//		}
//
//		result = new ModelAndView("sponsorship/edit");
//		result.addObject("sponsorship", sponsorship);
//		result.addObject("isPrincipal", isPrincipal);
//		result.addObject("message", messageCode);
//
//		return result;
//	}
}
