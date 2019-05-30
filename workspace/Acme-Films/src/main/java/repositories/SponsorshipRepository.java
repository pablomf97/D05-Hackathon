package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer>{

	@Query("select s from Sponsorship s join s.films f where f.id = ?1")
	Collection<Sponsorship> sponsorshipsPerFilm(int filmId);
}
