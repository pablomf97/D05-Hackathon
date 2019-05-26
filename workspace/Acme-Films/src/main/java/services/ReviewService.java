package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import domain.Actor;
import domain.Review;

import repositories.ReviewRepository;

@Transactional
@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	
//	@Autowired
//	private ActorService				actorService;
	
	@Autowired
	private Validator					validator;
	
	
	// Simple CRUD methods -----------------------------------

	public Review create() {
	//	Actor principal;
		Review result;

//		principal = this.actorService.findByPrincipal();
//		Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC"), "not.allowed");

		result = new Review();

		result.setCreationDate(new Date(System.currentTimeMillis() - 1));
		result.setStatus("PENDING");


		return result;
	}
	public Collection<Review> findAll() {
		Collection<Review> result;
		result = this.reviewRepository.findAll();

		return result;
	}

	public Review findOne(final int reviewId) {
		Review result;
		result = this.reviewRepository.findOne(reviewId);

		return result;
	}
	
	public void delete(final Review review) {
		//Actor principal;

		Assert.notNull(review);
		Assert.isTrue(review.getId() != 0, "wrong.id");
//
//		principal = this.actorService.findByPrincipal();
//		Assert.isTrue(this.actorService.checkAuthority(principal, "ROOKIE"), "not.allowed");
//
//		Assert.isTrue(application.getRookie().equals(principal), "not.allowed");

		this.reviewRepository.delete(review.getId());

	}
	
	
	public Review save(final Review review) {
		Actor principal;
		Review result;

//		principal = this.actorService.findByPrincipal();
//		Assert.notNull(principal, "not.allowed");
//		Assert.notNull(review);

		Assert.isTrue(review.getFilm().getIsDraft()==false);
		
	
		result = this.reviewRepository.save(review);
		Assert.notNull(result);

		return result;
	}



	
	
	
}