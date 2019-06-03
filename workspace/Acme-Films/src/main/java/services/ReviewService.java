package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReviewRepository;
import domain.Actor;
import domain.Review;

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
		//result.setCritic(principal);
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
//		Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC"), "not.allowed");
//		Assert.isTrue(review.getIsDraft()==true);	
		//Assert.isTrue(review.getCritic().equals((Critic)principal));

		this.reviewRepository.delete(review.getId());

	}
	
	
	public Review save(final Review review) {
		Actor principal;
		Review result;

//		principal = this.actorService.findByPrincipal();
//		Assert.notNull(principal, "not.allowed");
//		Assert.notNull(review);

		
		if(review.getId()==0){
			Assert.isTrue(review.getFilm().getIsDraft()==true);
			//Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC");
			Assert.isTrue(review.getModerator()==null);
			Assert.isTrue(review.getRejectReason()==null || review.getRejectReason().isEmpty());
			Assert.isTrue(review.getFilm()!=null);
			Assert.isTrue(!this.findOne(review.getId()).getStatus().equals("ACCEPTED"));
			Assert.isTrue(!this.findOne(review.getId()).getStatus().equals("REJECTED"));
			
			
			
		}else{
			Assert.isTrue(review.getFilm()!=null);
			//Assert.isTrue(review.getCritic().equals((Critic)principal));
			//Puede asignarselo el moderador que quiera o el moderador de esa peli¿?
			
			
		}
	
		result = this.reviewRepository.save(review);
		Assert.notNull(result);

		return result;
	}
	
	
	// Other business methods -------------------------------

		public Review reconstruct(final Review review, final BindingResult binding) {
			Review result = this.create();

			if (review.getId() == 0) {
				result = new Review();

				result.setFilm(review.getFilm());

			} else {
				final Review orig = this.findOne(review.getId());
				result.setBody(orig.getBody());
				result.setCritic(orig.getCritic());
				result.setModerator(orig.getModerator());
				result.setFilm(orig.getFilm());
				result.setCreationDate(orig.getCreationDate());
				result.setId(orig.getId());
				result.setIsDraft(orig.getIsDraft());
				result.setRating(orig.getRating());
				result.setRejectReason(orig.getRejectReason());
				result.setTitle(orig.getTitle());
				
		
			}

			this.validator.validate(result, binding);

			return result;
		}

	
	
	
	
}
