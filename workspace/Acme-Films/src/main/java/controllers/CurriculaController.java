
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
import services.CurriculaService;
import services.PersonalDataService;
import domain.Actor;
import domain.Curricula;
import domain.EducationData;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.ProfessionalData;

@Controller
@RequestMapping("/curricula/critic")
public class CurriculaController extends AbstractController {

	//Services

	@Autowired
	private CurriculaService	curriculaService;

	@Autowired
	private PersonalDataService	personalService;

	@Autowired
	private ActorService		actorService;


	//Edition

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;

		try {
			result = new ModelAndView("curricula/edit");
			final PersonalData ps = this.personalService.create();
			result.addObject("personalData", ps);
		} catch (final IllegalArgumentException oopsi) {
			result = new ModelAndView("redirect:list.do");
		}

		return result;

	}

	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int curriculaId) {
		Curricula curricula;
		Collection<MiscellaneousData> miscellaneousData;
		Collection<EducationData> educationData;
		Collection<ProfessionalData> professionalData;
		PersonalData personalData;
		ModelAndView result;

		boolean emptyMiscellaneous, emptyEducation, emptyProfessional;
		try {
			curricula = this.curriculaService.findOne(curriculaId);
			Assert.notNull(curricula);
			miscellaneousData = curricula.getMiscellaneousData();
			educationData = curricula.getEducationData();
			professionalData = curricula.getProfessionalData();
			personalData = curricula.getPersonalData();

			result = new ModelAndView("curricula/display");
			try {
				final Actor principal = this.actorService.findByPrincipal();
				result.addObject("principal", principal);
			} catch (final Throwable oops) {
			}
			if (!(miscellaneousData.isEmpty())) {
				emptyMiscellaneous = false;
				result.addObject("miscellanousData", miscellaneousData.iterator().next());
			} else {
				emptyMiscellaneous = true;
				result.addObject("miscellaneousData", miscellaneousData);
			}

			if (!(educationData.isEmpty())) {
				emptyEducation = false;
				result.addObject("educationData", educationData.iterator().next());

			} else {
				emptyEducation = true;
				result.addObject("educationData", educationData);
			}

			if (!(professionalData.isEmpty())) {
				emptyProfessional = false;
				result.addObject("professionalData", professionalData.iterator().next());
			} else {
				emptyProfessional = true;
				result.addObject("professionalData", professionalData);
			}

			result.addObject("emptyMiscellaneous", emptyMiscellaneous);
			result.addObject("emptyEducation", emptyEducation);
			result.addObject("emptyProfessional", emptyProfessional);
			result.addObject("curricula", curricula);
			result.addObject("personalData", personalData);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "problem.commit.error");
		}
		return result;

	}

	//Delete

	@RequestMapping(value = "/display", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Curricula curricula, final BindingResult binding) {
		ModelAndView result = null;
		try {
			final Curricula db = this.curriculaService.findOne(curricula.getId());

			this.curriculaService.deleteCV(db);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "problem.commit.error");
		}
		return result;
	}

}
