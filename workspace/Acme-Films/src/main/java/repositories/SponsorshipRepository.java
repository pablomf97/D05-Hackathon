package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends
		JpaRepository<Sponsorship, Integer> {

	@Query("select f from Sponsorship f where f.sponsor.id = ?1 ")
	Collection<Sponsorship> sponsorshipPerSponsor(int id);

	@Query("select s from Sponsorship s join s.films f where f.id = ?1")
	Collection<Sponsorship> sponsorshipsPerFilm(int filmId);
	
	@Query("select s from Sponsorship s where s.sponsor.id = ?1")
	Collection<Sponsorship> sponsorshipsPerSponsor(int sponsorId);
	
	@Query("select s from Sponsorship s where s.isActive = null")
	Collection<Sponsorship> sponsorshipsToReview();
}
