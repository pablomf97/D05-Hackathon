package controllers;

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
import services.PersonService;
import domain.Actor;
import domain.Film;
import domain.Person;

@Controller
@RequestMapping("/person")
public class PersonController extends AbstractController {

	@Autowired
	private ActorService	actorService;

	@Autowired
	private PersonService		personService;

	@Autowired
	private FilmService		filmService;
	
	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int personId) {
		ModelAndView result;
		Person person;
		boolean isPrincipal = false;
		Actor principal;
		Collection<Film> films;
		
		try {
			person = this.personService.findOne(personId);

			//TODO: list of films where a person works
			films = this.filmService.filmsOfPerson(personId);
			
			result = new ModelAndView("person/display");
			result.addObject("person", person);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("requestURI", "person/display.do?personId=" + personId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "position.commit.error");
			result.addObject("permission", false);
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam (required = false) Integer filmId) {
		final ModelAndView result = new ModelAndView("person/list");
		Collection<Person> persons = this.personService.findAll();
		Actor principal;
		boolean isPrincipal;

		try {		
			if (filmId == null)
				persons = this.personService.findAll();
			else {
				persons  = this.filmService.findOne(filmId).getPersons();				
			}
			
			result.addObject("persons", persons);
			result.addObject("isPrincipal", true);

		} catch (final Throwable oops) {
			result.addObject("errMsg", oops);
			result.addObject("isPrincipal", false);
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result = null;
		try {
			final Person person = this.personService.create();

			result = this.createEditModelAndView(person);
		} catch (final Throwable oops) {
			System.out.println(oops.getMessage());
		}
		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int personId) {
		ModelAndView result;
		Person person;
		try {
			person = this.personService.findOne(personId);
			Assert.notNull(person);

			result = this.createEditModelAndView(person);
			result.addObject("personId", personId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Person person,
			BindingResult binding) {
		ModelAndView result;
		
		if (binding.hasErrors()) {
			result = new ModelAndView("person/edit");
			result.addObject("person", person);
			result.addObject("isPrincipal", true);
		} else {
			try {
				this.personService.save(person);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("person/edit");
				result.addObject("person", person);
				result.addObject("messageCode", oops.getMessage());
			}
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int personId) {
		ModelAndView result;
		try {
			final Person person = this.personService.findOne(personId);
			this.personService.delete(person);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageCode", oops.getMessage());
		}
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Person person) {
		ModelAndView result;

		result = this.createEditModelAndView(person, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Person person, final String messageCode) {
		final ModelAndView result;
		Actor principal;
		boolean isPrincipal = true;

		if (messageCode == null) {
			principal = this.actorService.findByPrincipal();

			if (!this.actorService.checkAuthority(principal, "MODERATOR"))
				isPrincipal = false;
		}

		result = new ModelAndView("person/edit");
		result.addObject("person", person);
		result.addObject("isPrincipal", isPrincipal);
		result.addObject("message", messageCode);

		return result;
	}
}
