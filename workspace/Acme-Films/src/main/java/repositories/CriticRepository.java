
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Critic;

@Repository
public interface CriticRepository extends JpaRepository<Critic, Integer> {


	@Query("select c from Critic c order by c.curricula.professionalData.size desc")
	Collection<Critic> top3CriticsMoreProfessional();

	@Query("select r.critic from Review r where r.rating= (select max(r.rating)from Review r)")
	Collection<Critic> criticsWithHighestRatingReview();

	@Query("select c from Critic c where c.userAccount.username = ?1")
	Critic findByUsername(String username);


	@Query("select cr from Critic cr where cr.curricula.id = ?1")
	Actor findCriticByCurriculaId(int id);

}
