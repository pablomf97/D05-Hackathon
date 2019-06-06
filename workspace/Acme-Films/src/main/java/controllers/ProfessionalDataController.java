
package controllers;

import java.util.Collection;

import javax.validation.Valid;

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
import services.CurriculaService;
import services.ProfesionalDataService;
import domain.Actor;
import domain.Critic;
import domain.Curricula;
import domain.ProfessionalData;

@Controller
@RequestMapping(value = "professionalData/critic")
public class ProfessionalDataController extends AbstractController {

	//Services
	@Autowired
	private ProfesionalDataService	professionalDataService;

	@Autowired
	private CurriculaService		curriculaService;

	@Autowired
	private Validator				validator;
	@Autowired
	private ActorService			actorService;


	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int curriculaId) {
		ModelAndView result;
		Curricula currentCurricula;
		Collection<ProfessionalData> professionalData;
		try {

			currentCurricula = this.curriculaService.findOne(curriculaId);

			professionalData = currentCurricula.getProfessionalData();

			result = new ModelAndView("professionalData/list");
			try {
				final Actor principal = this.actorService.findByPrincipal();
				result.addObject("principal", principal);
			} catch (final Throwable oops) {
			}
			result.addObject("currentCurricula", currentCurricula);
			result.addObject("professionalData", professionalData);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "problem.commit.error");
		}
		return result;
	}

	//Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int dataId, @RequestParam final int curriculaId) {
		ModelAndView result;
		ProfessionalData data;
		try {

			data = this.professionalDataService.findOne(dataId);

			result = new ModelAndView("professionalData/display");
			try {
				final Actor principal = this.actorService.findByPrincipal();
				result.addObject("principal", principal);
			} catch (final Throwable oops) {
			}

			result.addObject("data", data);
			result.addObject("curriculaId", curriculaId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "problem.commit.error");
		}
		return result;

	}

	//Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int dataId, @RequestParam final int curriculaId) {
		ModelAndView result = null;
		ProfessionalData data;

		try {
			this.professionalDataService.checkOwnerProfessionalData(dataId);

			data = this.professionalDataService.findOne(dataId);
			final Critic act = (Critic) this.actorService.findByPrincipal();
			Assert.isTrue(act.getCurricula().getId() == curriculaId);
			result = this.createEditModelAndView(data, curriculaId);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "problem.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ProfessionalData data, final int curriculaId, final BindingResult binding) {
		ModelAndView result;
		try {
			try {
				if (!(data.getEndDate() == null))
					Assert.isTrue(data.getStartDate().before(data.getEndDate()));
			} catch (final Throwable oops) {
				binding.rejectValue("startDate", "date.error");
			}
			this.validator.validate(data, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(data, curriculaId, "md.commit.error");
			else
				try {
					if (data.getId() != 0)
						this.professionalDataService.checkOwnerProfessionalData(data.getId());
					this.professionalDataService.save(data, curriculaId);
					final Curricula currentCurricula = this.curriculaService.findOne(curriculaId);

					result = new ModelAndView("redirect:list.do?curriculaId=" + currentCurricula.getId());
				} catch (final Throwable oops) {
					result = new ModelAndView("redirect:list.do?curriculaId=" + curriculaId);
					result.addObject("messageCode", "problem.commit.error");
				}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "problem.commit.error");
		}
		return result;

	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final ProfessionalData data, final BindingResult binding) {
		ModelAndView result;
		try {
			if (binding.hasErrors())
				result = this.createEditModelAndView(data, null, null);
			else
				try {
					final Curricula currentCurricula = this.curriculaService.getCurriculaByProfessionalData(data.getId());
					this.professionalDataService.delete(data);
					result = new ModelAndView("redirect:list.do?curriculaId=" + currentCurricula.getId());
				} catch (final Throwable oops) {
					result = new ModelAndView("redirect:../../welcome/index.do");
					result.addObject("messageCode", "problem.commit.error");
				}
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "problem.commit.error");
		}
		return result;

	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculaId) {
		ModelAndView result = null;
		try {
			final ProfessionalData data = this.professionalDataService.create();
			final Critic act = (Critic) this.actorService.findByPrincipal();
			Assert.isTrue(act.getCurricula().getId() == curriculaId);
			result = this.createEditModelAndView(data, curriculaId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:../../welcome/index.do");
			result.addObject("messageCode", "problem.commit.error");
		}

		return result;

	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final ProfessionalData data, final int curriculaId) {
		ModelAndView result;

		result = this.createEditModelAndView(data, curriculaId, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(final ProfessionalData data, final Integer curriculaId, final String messageError) {
		ModelAndView result;
		Curricula currentCurricula;

		result = new ModelAndView("professionalData/edit");

		if (!(curriculaId == null)) {
			currentCurricula = this.curriculaService.findOne(curriculaId);
			result.addObject("currentCurricula", currentCurricula);
		}

		result.addObject("professionalData", data);
		result.addObject("message", messageError);

		return result;
	}
}
