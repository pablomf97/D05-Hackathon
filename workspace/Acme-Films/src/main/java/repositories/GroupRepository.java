
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Forum;

@Repository
public interface GroupRepository extends JpaRepository<Forum, Integer> {

	@Query("select f from Forum f where f.filmAbout.id= ?1 and f.isActive = 1")
	Collection<Forum> forumsPerFilm(int id);

	@Query("select f from Forum f where f.sagaAbout.id= ?1 and f.isActive = 1")
	Collection<Forum> forumsPerSaga(int id);

	@Query("select f from Forum f where f.creator.id= ?1")
	Collection<Forum> forumsPerFilmEnthusiast(int id);
	


}
