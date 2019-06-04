package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {

	@Query("select max(1.0*(select count(*) from Comment c where c.film=f)),min(1.0*(select count(*) from Comment c where c.film=f)),avg(1.0*(select count(*) from Comment c where c.film=f)),stddev(1.0*(select count(*) from Comment c where c.film=f)) from Film f")
	Double[] statsCommentsPerFilm();

	@Query("select max(f.persons.size),min(f.persons.size),avg(f.persons.size),stddev(f.persons.size) from Film f")
	Double[] statsPersonsPerFilm();

	@Query("select f from Film f where f.rating= (select max(f.rating)from Film f)")
	Collection<Film> filmsWithHighestRating();

	@Query("select (sum(case when r.isDraft='0' then 1.0 else 0 end)/count(*)) from Film r")
	Double ratioFinalModeFilms();

	@Query("select p from Film p order by p.runTime desc")
	Collection<Film> top5FilmsWithMoreRunTime();

	@Query("select max(1.0*(select count(*) from Visualization c where c.film=f)),min(1.0*(select count(*) from Visualization c where c.film=f)),avg(1.0*(select count(*) from Visualization c where c.film=f)),stddev(1.0*(select count(*) from Visualization c where c.film=f)) from Film f")
	Double[] statsPointsVisualizationPerFilm();

	@Query("select f from Film f join f.sagas s where s.id = ?1")
	Collection<Film> filmsOfSaga(int sagaId);

	@Query("select f from Film f where f.moderator.id = ?1")
	Collection<Film> filmsByModerator(int moderatorId);

	
	@Query("select f from Film f join f.persons p where p.id = ?1")
	Collection<Film> filmsOfPerson(int personId);
	
	@Query("select f from Film f join f.genres g where g.id = ?1")
	Collection<Film> filmsWithGenre(int genreId);

	@Query("select f from Film f where f.isDraft = false")
	Collection<Film> publishedFilms();
	
	@Query("select f from Film f join f.sagas s where s.id = ?1")
	Collection<Film> filmsWithSaga(int sagaId);
	
}
