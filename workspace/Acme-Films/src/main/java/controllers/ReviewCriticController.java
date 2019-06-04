package controllers;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ReviewService;
import domain.Actor;
import domain.Critic;
import domain.Film;
import domain.Review;

@Controller
@RequestMapping("review/critic")
public class ReviewCriticController extends AbstractController {

	// Services

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ActorService actorService;

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Review review = this.reviewService.create();

		result = this.createEditModelAndView(review);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int reviewId) {
		ModelAndView result;
		Review review;

		review = this.reviewService.findOne(reviewId);

		result = this.createEditModelAndView(review);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "saveDraft2")
	public ModelAndView saveDraft2(Review review, BindingResult binding) {
		ModelAndView result;
		boolean finalMode = false;
		Boolean accepted = null;

		try {
			review = this.reviewService.reconstruct(review, binding);

			this.reviewService.save(review, finalMode, accepted);
			// TODO: redirect
			result = new ModelAndView("redirect:listAll.do");
		}catch(ValidationException oops){
			result = this.createEditModelAndView(review);
		} catch (Throwable oops) {
			result = this.createEditModelAndView(review, "Commir error");
		}


		return result;

	}


	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveDraft")
	public ModelAndView saveDraft(Review review, BindingResult binding) {
		ModelAndView result;
		boolean finalMode = false;
		Boolean accepted = null;


		try {
			review = this.reviewService.reconstruct(review, binding);

			this.reviewService.save(review, finalMode, accepted);
			// TODO: redirect
			result = new ModelAndView("redirect:listAll.do");
		}catch(ValidationException oops){
			result = this.createEditModelAndView(review);
		} catch (Throwable oops) {
			result = this.createEditModelAndView(review, "Commit error");
		}

		return result;
	}


	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "saveFinal2")
	public ModelAndView saveFinal2(Review review, BindingResult binding) {
		ModelAndView result;
		boolean finalMode = true;
		Boolean accepted = null;


		try {

			review = this.reviewService.reconstruct(review, binding);

			this.reviewService.save(review, finalMode, accepted);
			// TODO: redirect
			result = new ModelAndView("redirect:listAll.do");
		}catch(ValidationException oops){
			result = this.createEditModelAndView(review);
		} catch (Throwable oops) {
			result = this.createEditModelAndView(review, "Commit error");

		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(Review review, BindingResult binding) {
		ModelAndView result;
		boolean finalMode = true;
		Boolean accepted = null;

		try {
			review = this.reviewService.reconstruct(review, binding);

			this.reviewService.save(review, finalMode, accepted);
			// TODO: redirect
			result = new ModelAndView("redirect:listAll.do");
		}catch(ValidationException oops){
			result = this.createEditModelAndView(review);
		} catch (Throwable oops) {
			result = this.createEditModelAndView(review, "Commit error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Review review, BindingResult binding) {
		ModelAndView result;


		try {
			review = this.reviewService.reconstruct(review, binding);

			this.reviewService.delete(review);
			// TODO: redirect
			result = new ModelAndView("redirect:listAll.do");
		}catch(ValidationException oops){
			result = this.createEditModelAndView(review);
		} catch (Throwable oops) {
			result = this.createEditModelAndView(review, "Commir error");
		}

		return result;
	}

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		Actor principal = this.actorService.findByPrincipal();
		Collection<Review> reviews = this.reviewService
				.getReviewsByCritic(principal.getId());
		boolean possible = false;

		if (principal.getId() == reviews.iterator().next().getCritic().getId()) {
			possible = true;
		}
		result = new ModelAndView("review/listAll");
		result.addObject("reviews", reviews);
		result.addObject("possible", possible);

		return result;
	}


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int reviewId) {
		ModelAndView result;
		Review review = this.reviewService.findOne(reviewId);
		boolean possible = false;

		Actor principal = this.actorService.findByPrincipal();

		if (review.getCritic().getId() == principal.getId()) {
			possible = true;
		}

		result = new ModelAndView("review/display");
		result.addObject("possible", possible);
		result.addObject("review", review);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Review review) {
		ModelAndView result;

		result = this.createEditModelAndView(review, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Review review,
			final String messageCode) {
		ModelAndView result;
		Collection<Film> finalFilms = this.reviewService.getFinalFilms();
		boolean possible = false;
		Actor principal = this.actorService.findByPrincipal();

		review.setCritic((Critic)principal);

		if (review.getCritic().equals((Critic) principal)) {
			possible = true;
		}

		result = new ModelAndView("review/edit");

		result.addObject("finalFilms", finalFilms);
		result.addObject("possible", possible);
		result.addObject("review", review);
		result.addObject("messageCode", messageCode);

		return result;
	}
}
