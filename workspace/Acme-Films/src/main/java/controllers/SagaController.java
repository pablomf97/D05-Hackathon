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
import services.SagaService;
import domain.Actor;
import domain.Film;
import domain.Saga;

@Controller
@RequestMapping("/saga")
public class SagaController extends AbstractController {

	@Autowired
	private ActorService	actorService;

	@Autowired
	private SagaService		sagaService;
	
	@Autowired
	private FilmService		filmService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int sagaId) {
		ModelAndView result;
		Saga saga;
		boolean isPrincipal = false;
		Collection<Film> films;

		try {
			saga = this.sagaService.findOne(sagaId);
			films = this.filmService.filmsOfSaga(sagaId);

			result = new ModelAndView("saga/display");
			result.addObject("saga", saga);
			result.addObject("films", films);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "saga/display.do?sagaId=" + sagaId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "position.commit.error");
			result.addObject("permission", false);
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("saga/list");
		Collection<Saga> sagas = this.sagaService.findAll();
		Collection<Saga> noDelete;
		Actor principal;
		boolean isPrincipal = false;

		try {
			principal = this.actorService.findByPrincipal();
			if (this.actorService.checkAuthority(principal, "MODERATOR"))
				isPrincipal = true;
			
			noDelete = this.sagaService.sagasInUse();

			result.addObject("sagas", sagas);
			result.addObject("noDelete", noDelete);
			result.addObject("isPrincipal", isPrincipal);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops);
			result.addObject("isPrincipal", isPrincipal);
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = null;
		Actor principal;
		
		try {
			principal = this.actorService.findByPrincipal();
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"MODERATOR"));
			
			final Saga saga = this.sagaService.create();

			result = this.createEditModelAndView(saga);
		} catch (final Throwable oops) {
			System.out.println(oops.getMessage());
		}
		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sagaId) {
		ModelAndView result;
		Saga saga;
		try {
			saga = this.sagaService.findOne(sagaId);
			Assert.notNull(saga);

			result = this.createEditModelAndView(saga);
			result.addObject("sagaId", sagaId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Saga saga,
			BindingResult binding) {
		ModelAndView result;
		
		if (binding.hasErrors()) {
			result = new ModelAndView("saga/edit");
			result.addObject("saga", saga);
			result.addObject("isPrincipal", true);
		} else {
			try {
				this.sagaService.save(saga);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("saga/edit");
				result.addObject("saga", saga);
				result.addObject("messageCode", oops.getMessage());
			}
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int sagaId) {
		ModelAndView result;
		Collection<Saga> noDelete = new ArrayList<>();
		try {
			final Saga saga = this.sagaService.findOne(sagaId);
			
			noDelete = this.sagaService.sagasInUse();
			
			Assert.isTrue(!noDelete.contains(saga));
			
			this.sagaService.delete(saga);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", oops.getMessage());
		}
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Saga saga) {
		ModelAndView result;

		result = this.createEditModelAndView(saga, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Saga saga, final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isPrincipal = true;

		if (messageCode == null) {
			principal = this.actorService.findByPrincipal();

			if (!this.actorService.checkAuthority(principal, "MODERATOR"))
				isPrincipal = false;
		}

		result = new ModelAndView("saga/edit");
		result.addObject("saga", saga);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("message", messageCode);

		return result;
	}
}
