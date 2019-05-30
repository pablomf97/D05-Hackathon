package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Film;
import domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{

	@Query("select f from Film f where f.isDraft = true")
	public Collection<Film> finalFilms();
	
	@Query("select r from Review r where r.critic.id = ?1")
	public Collection<Review> getReviewsByCritic(int criticId);
	
	@Query("select r from Review r where r.critic.id = ?1 and r.isDraft = false")
	public Collection<Review> getReviewsFinalsByCritic(int criticId);
	
	@Query("select r from Review r where r.moderator = null and r.isDraft = false")
	public Collection<Review> getReviewsFinalsToAssign();
	
	@Query("select r from Review r where r.moderator.id = ?1 and r.isDraft = false")
	public Collection<Review> getMyReviews(int moderatorId);
	}

