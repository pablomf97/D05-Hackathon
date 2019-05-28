package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Moderator;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {

	@Query("select max(1.0*(select count(*) from Review c where c.moderator=f)),min(1.0*(select count(*) from Review c where c.moderator=f)),avg(1.0*(select count(*) from Review c where c.moderator=f)),stddev(1.0*(select count(*) from Review c where c.moderator=f)) from Moderator f")
	Double[] statsReviewsPerModerator();

	@Query("select m from Moderator m where m.userAccount.username = ?1")
	Moderator findByUsername(String username);

}
