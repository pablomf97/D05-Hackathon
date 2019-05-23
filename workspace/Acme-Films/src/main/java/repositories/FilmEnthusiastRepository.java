package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.FilmEnthusiast;

@Repository
public interface FilmEnthusiastRepository extends JpaRepository<FilmEnthusiast, Integer>{

}
