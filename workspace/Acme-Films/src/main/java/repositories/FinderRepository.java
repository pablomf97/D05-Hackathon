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
}
