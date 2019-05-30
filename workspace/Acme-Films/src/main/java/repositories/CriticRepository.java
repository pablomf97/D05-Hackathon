
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Critic;

@Repository
public interface CriticRepository extends JpaRepository<Critic, Integer> {

	@Query("select cr from Critic cr where cr.curricula.id = ?1")
	Actor findCriticByCurriculaId(int id);

}
