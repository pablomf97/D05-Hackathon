
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Curricula;

@Repository
public interface CurriculaRepository extends JpaRepository<Curricula, Integer> {

	@Query("select c from Curricula c join c.miscellaneousData m where m.id=?1")
	Curricula getCurriculaByMiscellaneousData(int dataId);

	@Query("select c from Curricula c join c.educationData e where e.id=?1")
	Curricula getCurriculaByEducationData(int dataId);

	@Query("select c from Curricula c join c.professionalData p where p.id=?1")
	Curricula getCurriculaByProfessionalData(int dataId);

	@Query("select c from Curricula c where c.personalData.id=?1")
	Curricula getCurriculaByPersonalData(int dataId);

}
