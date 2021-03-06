package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer>{

}
