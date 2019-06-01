
package controllers;

import java.util.ArrayList;
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
import services.SponsorshipService;
import domain.Actor;
import domain.Film;
import domain.Sponsor;
import domain.Sponsorship;

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
	public ModelAndView create(@RequestParam final int filmId) {
		ModelAndView result = null;
		Collection<Film> films;
		try {
			Sponsorship sponsorship = this.sponsorshipService.create();
			films = this.filmService.publishedFilms();

			result = this.createEditModelAndView(sponsorship);
			result.addObject("films", films);
		} catch (final Throwable oops) {
			System.out.println(oops.getMessage());
		}
		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;
		try {
			sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			Assert.notNull(sponsorship);

			result = this.createEditModelAndView(sponsorship);
			result.addObject("sponsorshipId", sponsorshipId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;
		Sponsorship aux;
		try {
			aux = this.sponsorshipService.reconstruct(sponsorship, binding);
			if (binding.hasErrors()) {

				result = new ModelAndView("sponsorship/edit");
				result.addObject("sponsorship", sponsorship);
				result.addObject("binding", binding);
				result.addObject("isPrincipal", true);
			} else
				try {
					this.sponsorshipService.save(aux);
					result = new ModelAndView("redirect:list.do");
				} catch (final Throwable oops) {
					result = new ModelAndView("sponsorship/edit");
					result.addObject("sponsorship", aux);
					result.addObject("messageCode", oops.getMessage());
				}
		} catch (final Throwable oops) {
			if (binding.hasErrors())
				result = this.createEditModelAndView(sponsorship, "jpa.error");
			else
				result = this.createEditModelAndView(sponsorship, "commit.error");
		}
		return result;
	}

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
	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;

		result = this.createEditModelAndView(sponsorship, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isPrincipal = true;

		if (messageCode == null) {
			principal = this.actorService.findByPrincipal();

			if (!sponsorship.getSponsor().equals((Sponsor) principal))
				isPrincipal = false;
		}

		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorship", sponsorship);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("message", messageCode);

		return result;
	}
}
