package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ReviewService;
import domain.Actor;
import domain.Review;

@Controller
@RequestMapping(value="/review")
public class ReviewController extends AbstractController{
	
	// Services

		@Autowired
		private ReviewService reviewService;

		
		
		@RequestMapping(value = "/listByFilm", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam int filmId) {
			ModelAndView result;

			
			Collection<Review> reviews = this.reviewService.getReviewsByFilm(filmId);
			
			
			result = new ModelAndView("review/listByFilm");
			result.addObject("reviews", reviews);
			

			return result;
		}
		
		@RequestMapping(value = "/displayNon", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam final int reviewId) {
			ModelAndView result;
			Review review = this.reviewService.findOne(reviewId);

			result = new ModelAndView("review/displayNon");
		
			result.addObject("review", review);

			return result;
		}
}
