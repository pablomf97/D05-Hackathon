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

	@Query("select distinct f from Film f join f.genres m join f.persons p, in (m.name) g where ( g like %?1% or p.name like %?1% or f.title like %?1% or f.synopsis like %?1% ) and  f.runTime <= ?2 and    f.rating >= ?3 and f.rating <= ?4 and f.isDraft ='0'")
	Collection<Film> search(String keyWord, Integer maximumDuration,Double minimumRating,Double maximumRating);

	@Query("select v.film from Visualization v join v.film f where v.siteName like %?1% and  f.runTime <= ?2 and    f.rating >= ?3 and f.rating <= ?4 and f.isDraft ='0'")
	Collection<Film> searchV(String keyWord, Integer maximumDuration,Double minimumRating,Double maximumRating);

	@Query("select (sum(case when m.results.size=0 then 1.0 else 0 end)/count(m)) from Finder m")
	Double RatioFindersEmpty();
	
	@Query("select max(h.finder.results.size), min(h.finder.results.size), avg(h.finder.results.size),sqrt(sum(h.finder.results.size* h.finder.results.size) / count(h.finder.results.size) -(avg(h.finder.results.size) * avg(h.finder.results.size))) from FilmEnthusiast h")
	Double[] StatsFinder();
}
