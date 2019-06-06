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
import services.ReviewService;
import domain.Actor;
import domain.Film;
import domain.Moderator;
import domain.Review;

@Controller
@RequestMapping("review/moderator")
public class ReviewModeratorController extends AbstractController {

	// Services
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ActorService actorService;

	// List to assign

	@RequestMapping(value = "/listToAssign", method = RequestMethod.GET)
	public ModelAndView listToAssign() {
		ModelAndView result;
		Collection<Review> reviews = this.reviewService
				.getReviewsFinalsToAssign();
		boolean possible = false;

		if (!(reviews.isEmpty())) {
			possible = true;
		}

		result = new ModelAndView("review/listToAssign");
		result.addObject("reviews", reviews);
		result.addObject("possible", possible);

		return result;
	}

	@RequestMapping(value = "/listMyReviews", method = RequestMethod.GET)
	public ModelAndView listMyReviews() {
		ModelAndView result;
		Actor principal = this.actorService.findByPrincipal();
		boolean possible = false;
		Collection<Review> reviews = this.reviewService.getMyReviews(principal
				.getId());

		if (!(reviews.isEmpty())) {
			if (reviews.iterator().next().getModerator().equals(principal)) {
				possible = true;
			}
		}

		result = new ModelAndView("review/listMyReviews");

		result.addObject("reviews", reviews);
		result.addObject("possible", possible);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int reviewId) {
		ModelAndView result;
		Review review = this.reviewService.findOne(reviewId);

		result = this.createEditModelAndView(review);

		return result;
	}

	// @RequestMapping(value = "/edit", method = RequestMethod.POST, params =
	// "accept")
	// public ModelAndView accept(@Valid Review review, BindingResult binding) {
	// ModelAndView result;
	//
	// if (binding.hasErrors()) {
	// result = this.createEditModelAndView(review);
	// } else {
	// try {
	// this.reviewService.save(review, true, true);
	// result = new ModelAndView("review/listMyReviews");
	// } catch (Throwable oops) {
	// result = this.createEditModelAndView(review, "commit error");
	// }
	// }
	//
	// return result;
	// }
	//
	// @RequestMapping(value = "/edit", method = RequestMethod.POST, params =
	// "reject")
	// public ModelAndView reject(@Valid Review review, BindingResult binding) {
	// ModelAndView result;
	//
	// if (binding.hasErrors()) {
	// result = this.createEditModelAndView(review);
	// } else {
	// try {
	// this.reviewService.save(review, true, false);
	// result = new ModelAndView("review/listMyReviews");
	// } catch (Throwable oops) {
	// result = this.createEditModelAndView(review, "commit error");
	// }
	// }
	//
	// return result;
	// }

	@RequestMapping(value = "/assign", method = RequestMethod.GET)
	public ModelAndView assign(@RequestParam final int reviewId) {

		ModelAndView result;

		Review validReview = this.reviewService.findOne(reviewId);

		try {
			this.reviewService.selfAssign(validReview);
			result = new ModelAndView("redirect:listMyReviews.do");
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:listToAssign.do");
		}

		return result;
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int reviewId) {

		BindingResult binding = null;

		ModelAndView result;

		Review validReview = this.reviewService.reconstructModerator(reviewId,
				binding);

		try {
			this.reviewService.save(validReview, true, true);
			result = new ModelAndView("redirect:listMyReviews.do");
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:listToAssign.do");
		}

		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int reviewId) {

		ModelAndView result;
		Actor pricipal = this.actorService.findByPrincipal();
		boolean possible = false;
		

		Review validReview = this.reviewService.findOne(reviewId);
		
		if(validReview.getModerator().equals(pricipal)){
			possible = true;
		}

		result = new ModelAndView("review/edit");

		result.addObject("review", validReview);

		result.addObject("possible", possible);


		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.POST, params = "reject")
	public ModelAndView reject(Review review, BindingResult binding) {

		ModelAndView result;

		Review validReview = this.reviewService.reconstruct(review, binding);

		if (binding.hasErrors()) {
			result = new ModelAndView("review/edit");
			result.addObject("validReview", validReview);
		} else {
			try {
				this.reviewService.save(validReview, true, false);
				result = new ModelAndView("redirect:listMyReviews.do");
			} catch (Throwable oops) {
				result = new ModelAndView("redirect:listToAssign.do");
			}
		}
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int reviewId) {
		ModelAndView result;
		Review review = this.reviewService.findOne(reviewId);
		Boolean possible = null;
		Actor principal = this.actorService.findByPrincipal();

		if (review.getModerator() != null) {
			if (principal.equals(review.getModerator())) {
				possible = true;
			} else {
				possible = false;
			}
		}

		result = new ModelAndView("review/display");
		result.addObject("review", review);
		result.addObject("possible", possible);

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
		boolean possible = false;

		try {
			Actor principal = this.actorService.findByPrincipal();

			if (review.getModerator().getId() == principal.getId()) {
				possible = true;
			}

			result = new ModelAndView("review/edit");

			result.addObject("possible", possible);
			result.addObject("review", review);
			result.addObject("messageCode", messageCode);
			result.addObject("ACCEPTED", "ACCEPTED");
			result.addObject("REJECTED", "REJECTED");
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
		}

		return result;
	}

}
