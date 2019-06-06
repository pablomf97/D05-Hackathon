package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReviewRepository;
import domain.Actor;
import domain.Critic;
import domain.Film;
import domain.Moderator;
import domain.Review;

@Transactional
@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	// Simple CRUD methods -----------------------------------

	public Review create() {
		Actor principal;
		Review result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC"),
				"not.allowed");

		result = new Review();
		result.setStatus("PENDING");
		result.setCritic((Critic) principal);
		result.setCreationDate(new Date(System.currentTimeMillis() - 1));
		result.setIsDraft(true);

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
		Actor principal;

		Assert.notNull(review);
		Assert.isTrue(review.getId() != 0, "wrong.id");

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC"),
				"not.allowed");
		Assert.isTrue(review.getIsDraft() == true);
		Assert.isTrue(review.getCritic().equals((Critic) principal));

		this.reviewRepository.delete(review.getId());

	}

	// public Review save(final Review review) {
	// Actor principal;
	// Review result;
	//
	// principal = this.actorService.findByPrincipal();
	// Assert.notNull(principal, "not.allowed");
	// Assert.notNull(review);
	//
	//
	// if(review.getId()==0){
	// Assert.isTrue(review.getFilm().getIsDraft()==true);
	// Assert.isTrue(this.actorService.checkAuthority(principal, "CRITIC");
	// Assert.isTrue(review.getModerator()==null);
	// Assert.isTrue(review.getRejectReason()==null ||
	// review.getRejectReason().isEmpty());
	// Assert.isTrue(review.getFilm()!=null);
	// Assert.isTrue(!this.findOne(review.getId()).getStatus().equals("ACCEPTED"));
	// Assert.isTrue(!this.findOne(review.getId()).getStatus().equals("REJECTED"));
	//
	//
	//
	// }else{
	// Assert.isTrue(review.getFilm()!=null);
	// Assert.isTrue(review.getCritic().equals((Critic)principal));
	// //Puede asignarselo el moderador que quiera o el moderador de esa peli¿?
	//
	//
	// }
	//
	// result = this.reviewRepository.save(review);
	// Assert.notNull(result);
	//
	// return result;
	// }

	public Review save(final Review review, boolean finalMode, Boolean accepted) {
		Review result = new Review();
		Actor principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);

		if (!(finalMode)) {
			if (this.actorService.checkAuthority(principal, "CRITIC")) {
				Assert.isTrue(review.getCritic().equals((Critic) principal));

				// Create new review saving draft mode
				if (review.getId() == 0) {
					Assert.isTrue(review.getFilm() != null);
					Assert.isTrue(review.getModerator() == null,
							"Not final mode");

					review.setIsDraft(true);
					review.setCreationDate(new Date(
							System.currentTimeMillis() - 1));

					result = this.reviewRepository.save(review);

					// Edit existent review saving draft mode
				} else {

					Review reviewBD = this.findOne(review.getId());

					Assert.isTrue(review.getFilm() != null);
					Assert.isTrue(review.getFilm().getIsDraft() == false,
							"Not final film");
					Assert.isTrue(review.getFilm().equals(reviewBD.getFilm()),
							"Wrong film");
					Assert.isTrue(review.getModerator() == null,
							"Not final mode");

					Assert.isTrue(reviewBD.getCritic().equals(
							(Critic) principal));
					Assert.isTrue(reviewBD.getIsDraft() == true);

					reviewBD.setBody(review.getBody());
					reviewBD.setTitle(review.getTitle());
					reviewBD.setRating(review.getRating());
					reviewBD.setIsDraft(true);
					reviewBD.setCreationDate(new Date(System
							.currentTimeMillis() - 1));

					result = this.reviewRepository.save(reviewBD);

				}

			}

		} else {
			if (this.actorService.checkAuthority(principal, "CRITIC")) {
				if (review.getId() == 0) {
					Assert.isTrue(review.getFilm() != null);
					Assert.isTrue(review.getFilm().getIsDraft() == false,
							"Not final film.");
					Assert.isTrue(review.getModerator() == null,
							"Not final mode");

					review.setIsDraft(false);
					review.setCreationDate(new Date(
							System.currentTimeMillis() - 1));
					review.setStatus("PENDING");

					result = this.reviewRepository.save(review);

					// Edit existent review saving draf mode
				} else {

					Review reviewBD = this.findOne(review.getId());

					Assert.isTrue(review.getFilm() != null);
					Assert.isTrue(review.getFilm().getIsDraft() == false,
							"Not final film");
					Assert.isTrue(review.getFilm().equals(reviewBD.getFilm()),
							"Wrong film");
					Assert.isTrue(review.getModerator() == null,
							"Not final mode");

					Assert.isTrue(!(review.getStatus().equals("ACCEPTED")));
					Assert.isTrue(!(review.getStatus().equals("REJECTED")));

					Assert.isTrue(reviewBD.getCritic().equals(
							(Critic) principal));
					Assert.isTrue(reviewBD.getIsDraft() == true);

					reviewBD.setBody(review.getBody());
					reviewBD.setTitle(review.getTitle());
					reviewBD.setRating(review.getRating());
					reviewBD.setIsDraft(false);
					reviewBD.setCreationDate(new Date(System
							.currentTimeMillis() - 1));

					result = this.reviewRepository.save(reviewBD);

				}
			} else if (this.actorService.checkAuthority(principal, "MODERATOR")) {

				Review reviewBD = this.findOne(review.getId());

				Assert.isTrue(reviewBD.getId() != 0);
				Assert.isTrue(reviewBD.getIsDraft() == false);

				if (accepted) {
					reviewBD.setStatus("ACCEPTED");
				} else {
					reviewBD.setStatus("REJECTED");
					Assert.notNull(review.getRejectReason());
					reviewBD.setRejectReason(review.getRejectReason());
				}

				Assert.isTrue(review.getCritic().equals(reviewBD.getCritic()),
						"Wrong critic");
				Assert.isTrue(reviewBD.getFilm().equals(review.getFilm()),
						"Wrong film");

				result = this.reviewRepository.save(reviewBD);
			}
		}
		return result;
	}

	// Other business methods -------------------------------

	public void selfAssign(Review review) {
		Assert.isTrue(!review.getIsDraft());
		review.setModerator((Moderator) this.actorService.findByPrincipal());

	}

	public Review reconstruct(final Review review, final BindingResult binding) {
		Actor principal = this.actorService.findByPrincipal();
		Review result;
		if (this.actorService.checkAuthority(principal, "CRITIC")) {
			result = this.create();

			if (review.getId() == 0) {

				result.setFilm(review.getFilm());
				result.setBody(review.getBody());
				result.setRating(review.getRating());
				result.setTitle(review.getTitle());

			} else {
				final Review orig = this.findOne(review.getId());

				review.setCritic(orig.getCritic());
				review.setModerator(orig.getModerator());
				review.setFilm(orig.getFilm());
				review.setCreationDate(orig.getCreationDate());
				review.setIsDraft(orig.getIsDraft());
				review.setRating(orig.getRating());
				review.setRejectReason(orig.getRejectReason());
				review.setStatus(orig.getStatus());

				result = review;

			}
		} else {
			result = new Review();

			Review bd = this.findOne(review.getId());

			try {
				Assert.notNull(review.getRejectReason());
				Assert.isTrue(review.getRejectReason().length() > 0);
				bd.setRejectReason(review.getRejectReason());
			} catch (Throwable oops) {
				binding.rejectValue("rejectReason", "reason.error");
			}

			result = bd;
		}

		this.validator.validate(result, binding);

		return result;
	}

	public Review reconstructModerator(int reviewId, BindingResult binding) {

		Review bd = this.findOne(reviewId);

		this.validator.validate(bd, binding);

		return bd;
	}

	public Collection<Film> getFinalFilms() {

		Collection<Film> result = this.reviewRepository.finalFilms();

		return result;
	}

	public Collection<Review> getReviewsByCritic(int criticId) {
		Collection<Review> result = this.reviewRepository
				.getReviewsByCritic(criticId);

		return result;
	}

	public Collection<Review> getReviewsFinalsByCritic(int criticId) {
		Collection<Review> result = this.reviewRepository
				.getReviewsFinalsByCritic(criticId);

		return result;
	}

	public Collection<Review> getReviewsFinalsToAssign() {
		Collection<Review> result = this.reviewRepository
				.getReviewsFinalsToAssign();

		return result;
	}

	public Collection<Review> getMyReviews(int moderatorId) {
		Collection<Review> result = this.reviewRepository
				.getMyReviews(moderatorId);

		return result;
	}

	public void deleteReviewsCritics(int id) {
		this.reviewRepository.deleteInBatch(this.reviewRepository
				.getReviewsByCritic(id));

	}

	public void deleteReviewPerModerator(int id) {

		this.reviewRepository.deleteInBatch(this.reviewRepository
				.reviewPerModerator(id));

	}

	public Collection<Review> getReviewsByFilm(int filmId) {
		Collection<Review> result = this.reviewRepository
				.getReviewsByFilm(filmId);

		return result;
	}

	public void deleteReviewPerFilm(int id) {

		this.reviewRepository.deleteInBatch(this.reviewRepository
				.reviewPerFilm(id));

	}
	 public Collection<Review> reviewPerFilm(int id){
		return  this.reviewRepository.reviewPerFilm(id);
	 }

	public void deleteReview(Review r) {
	this.reviewRepository.delete(r);
		
	}
}
