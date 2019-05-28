package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.FilmEnthusiast;

@Repository
public interface FilmEnthusiastRepository extends JpaRepository<FilmEnthusiast, Integer>{

	//The top 3 film enthusiast in terms of attended event.
	
	
}
