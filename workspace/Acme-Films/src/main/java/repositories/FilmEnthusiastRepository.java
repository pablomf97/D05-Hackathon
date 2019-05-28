package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.FilmEnthusiast;

@Repository
public interface FilmEnthusiastRepository extends
		JpaRepository<FilmEnthusiast, Integer> {

	@Query("select f from FilmEnthusiast f where f.userAccount.username = ?1")
	FilmEnthusiast findByUsername(String username);

}
