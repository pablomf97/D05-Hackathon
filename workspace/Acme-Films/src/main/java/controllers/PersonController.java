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
import services.PersonService;
import services.PositionService;
import domain.Actor;
import domain.Film;
import domain.Person;
import domain.Position;

@Controller
@RequestMapping("/person")
public class PersonController extends AbstractController {

	@Autowired
	private ActorService actorService;


	@Autowired
	private PersonService personService;

	@Autowired
	private FilmService filmService;

	@Autowired
	private PositionService positionService;


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


			films = this.filmService.filmsOfPerson(personId);

			result = new ModelAndView("person/display");
			result.addObject("person", person);
			result.addObject("isPrincipal", isPrincipal);


			result.addObject("films", films);

			result.addObject("requestURI", "person/display.do?personId="
					+ personId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../welcome/index.do");
			result.addObject("messageCode", "position.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Integer filmId) {
		final ModelAndView result = new ModelAndView("person/list");
		Collection<Person> persons = this.personService.findAll();
		Actor principal;
		boolean isPrincipal = false;

		try {
			if (filmId == null)
				persons = this.personService.findAll();
			else {
				persons = this.filmService.findOne(filmId).getPersons();
			}

			
			principal = this.actorService.findByPrincipal();
			if (this.actorService.checkAuthority(principal, "MODERATOR"))
				isPrincipal = true;


			result.addObject("persons", persons);
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
		Collection<Position> positions;
		Actor principal;
		boolean isPrincipal = false;
		
		try {
			final Person person = this.personService.create();
			
			positions = this.positionService.findAll();
			
			try {
				principal = this.actorService.findByPrincipal();
				if (this.actorService.checkAuthority(principal, "MODERATOR"))
					isPrincipal = true;

			} catch (Exception e) {
			}

			result = this.createEditModelAndView(person);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("positions", positions);
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
		boolean isPrincipal = false;
		Actor principal;
		Collection<Position> positions;
		try {
			person = this.personService.findOne(personId);
			Assert.notNull(person);

			positions = this.positionService.findAll();

			try {
				principal = this.actorService.findByPrincipal();
				if (this.actorService.checkAuthority(principal, "MODERATOR"))
					isPrincipal = true;

			} catch (Exception e) {
			}

			result = this.createEditModelAndView(person);
			result.addObject("personId", personId);
			result.addObject("isPrincipal", isPrincipal);
			result.addObject("positions", positions);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Person person,
			@RequestParam(value = "positionsArray", required = false) String[] positionsArray,
			BindingResult binding) {
		ModelAndView result;
		Collection<Position> positionsToSave = new ArrayList<>();
		
		try {
			positionsToSave = this.positionService.parsePositions(positionsArray);
			
			person.setPositions(positionsToSave);
			
		} catch (Exception e) {
			
		}
		
			Person toSave = this.personService.validate(person, binding);


		if (binding.hasErrors()) {
			Collection<Position> positions = this.positionService.findAll();

			result = new ModelAndView("person/edit");
			result.addObject("person", person);
			result.addObject("isPrincipal", true);
			result.addObject("positions", positions);
		} else {
			try {
				this.personService.save(toSave);
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

	protected ModelAndView createEditModelAndView(final Person person,
			final String messageCode) {
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
