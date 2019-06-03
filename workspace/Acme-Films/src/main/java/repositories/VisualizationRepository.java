package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Visualization;

@Repository
public interface VisualizationRepository extends JpaRepository<Visualization, Integer>{
	
	@Query("select v from Visualization v where v.film.id = ?1")
	Collection<Visualization> visualizationsPerFilm(int filmId);

	
	@Query("select v from Visualization v where v.film.id= ?1")
	Collection<Visualization> visPerFilm(int id);
	
	
}
