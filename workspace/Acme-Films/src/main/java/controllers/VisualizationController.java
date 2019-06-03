
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
import services.VisualizationService;
import domain.Actor;
import domain.Film;
import domain.Visualization;

@Controller
@RequestMapping("/visualization")
public class VisualizationController extends AbstractController {

	@Autowired
	private ActorService	actorService;

	@Autowired
	private VisualizationService		visualizationService;
	
	@Autowired
	private FilmService		filmService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int visualizationId) {
		ModelAndView result;
		Visualization visualization;
		boolean isPrincipal = false;
		Actor principal;

		try {
			visualization = this.visualizationService.findOne(visualizationId);
			try {
				principal = this.actorService.findByPrincipal();
				if (this.actorService.checkAuthority(principal, "MODERATOR"))
					isPrincipal = true;
			} catch (final Throwable oops) {}

			result = new ModelAndView("visualization/display");
			result.addObject("visualization", visualization);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "visualization/display.do?visualizationId=" + visualizationId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "position.commit.error");
			result.addObject("permission", false);
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int filmId) {
		final ModelAndView result = new ModelAndView("visualization/list");
		Collection<Visualization> visualizations;
		Actor principal;
		boolean isPrincipal = false;
		Film film;

		try {
			principal = this.actorService.findByPrincipal();
			if (this.actorService.checkAuthority(principal, "MODERATOR"))
				isPrincipal = true;

			visualizations = this.visualizationService.visualizationsPerFilm(filmId);
			film = this.filmService.findOne(filmId);

			result.addObject("visualizations", visualizations);
			result.addObject("film", film);
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
		Film film;
		try {
			Visualization visualization = this.visualizationService.create();
			film = this.filmService.findOne(filmId);
			visualization.setFilm(film);

			result = this.createEditModelAndView(visualization);
		} catch (final Throwable oops) {
			System.out.println(oops.getMessage());
		}
		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int visualizationId) {
		ModelAndView result;
		Visualization visualization;
		try {
			visualization = this.visualizationService.findOne(visualizationId);
			Assert.notNull(visualization);

			result = this.createEditModelAndView(visualization);
			result.addObject("visualizationId", visualizationId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Visualization visualization, final BindingResult binding) {
		ModelAndView result;
		Visualization aux;
		try {
			aux = this.visualizationService.reconstruct(visualization, binding);
			int visualId = aux.getFilm().getId();
			if (binding.hasErrors()) {

				result = new ModelAndView("visualization/edit");
				result.addObject("visualization", visualization);
				result.addObject("binding", binding);
				result.addObject("isPrincipal", true);
			} else
				try {
					this.visualizationService.save(aux);
					result = new ModelAndView("redirect:list.do?filmId=" + visualId);
				} catch (final Throwable oops) {
					result = new ModelAndView("visualization/edit");
					result.addObject("visualization", aux);
					result.addObject("messageCode", oops.getMessage());
				}
		} catch (final Throwable oops) {
			if (binding.hasErrors())
				result = this.createEditModelAndView(visualization, "jpa.error");
			else
				result = this.createEditModelAndView(visualization, "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int visualizationId) {
		ModelAndView result;
		try {
			final Visualization visualization = this.visualizationService.findOne(visualizationId);
			int visualId = visualization.getFilm().getId();
			this.visualizationService.delete(visualization);
			result = new ModelAndView("redirect:list.do?filmId=" + visualId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:welcome.do");
			result.addObject("messageCode", oops.getMessage());
		}
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Visualization visualization) {
		ModelAndView result;

		result = this.createEditModelAndView(visualization, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Visualization visualization, final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isPrincipal = true;

		if (messageCode == null) {
			principal = this.actorService.findByPrincipal();

			if (!this.actorService.checkAuthority(principal, "MODERATOR"))
				isPrincipal = false;
		}

		result = new ModelAndView("visualization/edit");
		result.addObject("visualization", visualization);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("message", messageCode);

		return result;
	}
}
