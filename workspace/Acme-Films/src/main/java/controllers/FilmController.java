
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
import services.GenreService;
import services.PersonService;
import services.SagaService;
import domain.Actor;
import domain.Film;
import domain.Genre;
import domain.Moderator;
import domain.Person;
import domain.Saga;

@Controller
@RequestMapping("/film")
public class FilmController extends AbstractController {

	@Autowired
	private ActorService	actorService;

	@Autowired
	private FilmService		filmService;
	
	@Autowired
	private PersonService		personService;
	
	@Autowired
	private SagaService		sagaService;
	
	@Autowired
	private GenreService		genreService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int filmId) {
		ModelAndView result;
		Film film;
		boolean isPrincipal = false;
		Actor principal;

		try {
			film = this.filmService.findOne(filmId);
			try {
				principal = this.actorService.findByPrincipal();
				if (this.actorService.checkAuthority(principal, "MODERATOR"))
					isPrincipal = true;
				
			} catch (final Throwable oops) {}

			result = new ModelAndView("film/display");
			result.addObject("film", film);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "film/display.do?filmId=" + filmId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "position.commit.error");
			result.addObject("permission", false);
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("film/list");
		Collection<Film> films = new ArrayList<>();
		Actor principal = null;
		boolean isPrincipal = false;

		try {
			
			try {
				principal = this.actorService.findByPrincipal();
				if (this.actorService.checkAuthority(principal, "MODERATOR")) {
					isPrincipal = true;
					films = this.filmService.findFilmsPublishedAndMine(principal.getId());
				}
					
			} catch (Exception e) {}
			
			if(principal == null || !(this.actorService.checkAuthority(principal, "MODERATOR"))) {
				films = this.filmService.publishedFilms();
			}
			
			result.addObject("films", films);
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
		try {
			final Film film = this.filmService.create();

			result = this.createEditModelAndView(film);
		} catch (final Throwable oops) {
			System.out.println(oops.getMessage());
		}
		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int filmId) {
		ModelAndView result;
		Film film;
		Actor principal = null;
		boolean isPrincipal = false;
		
		try {
			film = this.filmService.findOne(filmId);
			Assert.notNull(film);
			
			try {
				principal = this.actorService.findByPrincipal();
				if(film.getModerator().equals((Moderator) principal) || 
						(!film.getIsDraft() && this.actorService.checkAuthority(principal, "MODERATOR"))) {
					isPrincipal = true;
				}
			} catch (Exception e) {}
			
			result = this.createEditModelAndView(film);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("filmId", filmId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Film film, BindingResult binding,
			@RequestParam(value = "personsArray", required = false) String[] personsArray, 
			@RequestParam(value = "genresArray", required = false) String[] genresArray) {
		ModelAndView result;
		Film aux;
		Collection<Person> personsToSave = new ArrayList<>();
		Collection<Genre> genresToSave = new ArrayList<>();
		
		try {
			try {
				personsToSave = this.personService.parsePersons(personsArray);
				if(genresArray != null) {
					genresToSave = this.genreService.parseGenres(genresArray);
				}
						
				film.setPersons(personsToSave);
				film.setGenres(genresToSave);
			} catch (Exception e) {}
			
			aux = this.filmService.reconstruct(film, binding);
			if (binding.hasErrors()) {
				
				film.setIsDraft(aux.getIsDraft());

				result = new ModelAndView("film/edit");
				result.addObject("film", film);
				result.addObject("binding", binding);
				result.addObject("isPrincipal", true);
				result.addObject("sagas", this.sagaService.findAll());
				result.addObject("genres", this.genreService.findAll());
				result.addObject("persons", this.personService.findAll());
			} else
				try {
					this.filmService.save(aux);
					result = new ModelAndView("redirect:list.do");
				} catch (final Throwable oops) {
					result = new ModelAndView("film/edit");
					result.addObject("film", aux);
					result.addObject("messageCode", oops.getMessage());
				}
		} catch (final Throwable oops) {
			if (binding.hasErrors())
				result = this.createEditModelAndView(film, "jpa.error");
			else
				result = this.createEditModelAndView(film, "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(final Film film, final BindingResult binding,
			@RequestParam(value = "personsArray", required = false) String[] personsArray, 
			@RequestParam(value = "genresArray", required = false) String[] genresArray) {
		ModelAndView result;
		Film aux;
		Collection<Person> personsToSave = new ArrayList<>();
		Collection<Genre> genresToSave = new ArrayList<>();
		try {
			
			try {
				personsToSave = this.personService.parsePersons(personsArray);
				if(genresArray != null) {
					genresToSave = this.genreService.parseGenres(genresArray);
				}
				film.setPersons(personsToSave);
				film.setGenres(genresToSave);
			} catch (Exception e) {}
			
			aux = this.filmService.reconstruct(film, binding);
			if (binding.hasErrors()) {
				
				film.setIsDraft(aux.getIsDraft());

				result = new ModelAndView("film/edit");
				result.addObject("film", film);
				result.addObject("binding", binding);
				result.addObject("isPrincipal", true);
				result.addObject("sagas", this.sagaService.findAll());
				result.addObject("genres", this.genreService.findAll());
				result.addObject("persons", this.personService.findAll());
			} else
				try {
					aux.setIsDraft(false);
					this.filmService.save(aux);
					result = new ModelAndView("redirect:list.do");
				} catch (final Throwable oops) {
					result = new ModelAndView("film/edit");
					result.addObject("film", aux);
					result.addObject("messageCode", oops.getMessage());
				}
		} catch (final Throwable oops) {
			if (binding.hasErrors())
				result = this.createEditModelAndView(film, "jpa.error");
			else
				result = this.createEditModelAndView(film, "commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int filmId) {
		ModelAndView result;
		try {
			final Film film = this.filmService.findOne(filmId);
			this.filmService.delete(film);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", oops.getMessage());
		}
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Film film) {
		ModelAndView result;

		result = this.createEditModelAndView(film, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Film film, final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isPrincipal = true;
		Collection<Saga> sagas = this.sagaService.findAll();
		Collection<Person> persons = this.personService.findAll();
		Collection<Genre> genres = this.genreService.findAll();

		if (messageCode == null) {
			principal = this.actorService.findByPrincipal();

			if (!this.actorService.checkAuthority(principal, "MODERATOR"))
				isPrincipal = false;
		}

		result = new ModelAndView("film/edit");
		result.addObject("film", film);
		result.addObject("sagas", sagas);
		result.addObject("genres", genres);
		result.addObject("persons", persons);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("message", messageCode);

		return result;
	}
}
