package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Film;
import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer>{


	@Query("select f from Film f where f.isDraft='0'")
	Collection<Film> allFilmsFinal();
	@Query("select distinct f from Film f join f.sagas m, in (f.title) p where p like %?1% and m.title like %?1% and  f.runTime <= ?2 and    f.avgReviewsRating >= ?3 and f.avgReviewsRating <= ?4 and f.isDraft ='0'")
	Collection<Film> searchTitleSaga(String keyWord, Double maximumDuration,Double minimumRating,Double maximumRating);
	//mirar si busca cuando no tenga un genero por ejemplo
	@Query("select distinct f from Film f join f.genres m, in (m.name) p where p like %?1% and  f.runTime <= ?2 and    f.avgReviewsRating >= ?3 and f.avgReviewsRating <= ?4 and f.isDraft ='0'")
	Collection<Film> searchG(String keyWord, Double maximumDuration,Double minimumRating,Double maximumRating);
	@Query("select distinct f from Film f, in (f.synopsis)  p where p like %?1% and  f.runTime <= ?2 and    f.avgReviewsRating >= ?3 and f.avgReviewsRating <= ?4 and f.isDraft ='0'")
	Collection<Film> searchS(String keyWord, Double maximumDuration,Double minimumRating,Double maximumRating);
	@Query("select v.film from Visualization v join v.film f where v.siteName like %?1% and f.isDraft='1'and  f.runTime <= ?2 and    f.avgReviewsRating >= ?3 and f.avgReviewsRating <= ?4 and f.isDraft ='0'")
	Collection<Film> searchV(String keyWord, Double maximumDuration,Double minimumRating,Double maximumRating);
	
	@Query("select (sum(case when m.results.size=0 then 1.0 else 0 end)/count(m)) from Finder m")
	Double RatioFindersEmpty();
	
	@Query("select max(h.finder.results.size), min(h.finder.results.size), avg(h.finder.results.size),sqrt(sum(h.finder.results.size* h.finder.results.size) / count(h.finder.results.size) -(avg(h.finder.results.size) * avg(h.finder.results.size))) from FilmEnthusiast h")
	Double[] StatsFinder();
}
