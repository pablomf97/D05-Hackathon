package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.ProfessionalData;

@Repository
public interface ProfessionalDataRepository extends JpaRepository<ProfessionalData, Integer>{

}
