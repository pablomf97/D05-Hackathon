package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {

	@Query("select max(1.0*(select count(*) from Sponsorship c where c.sponsor=f)),min(1.0*(select count(*) from Sponsorship c where c.sponsor=f)),avg(1.0*(select count(*) from Sponsorship c where c.sponsor=f)),stddev(1.0*(select count(*) from Sponsorship c where c.sponsor=f)) from Sponsor f")
	Double[] statsSponsorshipsPerSponsor();

	@Query("select s from Sponsor s where s.userAccount.username = ?1")
	Sponsor findByUsername(String username);

}
